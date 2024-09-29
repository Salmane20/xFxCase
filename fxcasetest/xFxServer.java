
import java.net.*;
import java.io.*;
import java.util.*;

public class xFxServer {
	
	private static final int PORT = 3221;
	
	public static void main(String[] args) throws Exception {
		
		try(ServerSocket ss = new ServerSocket(PORT)){
			System.out.println("Server is Bounded to Port " + PORT);
			while(true) {
				try(Socket ConnectionFromClient = ss.accept()){
					System.out.println("Server connected to Client with IP" + ConnectionFromClient.getPort());
					
					InputStream input = ConnectionFromClient.getInputStream();
					OutputStream output = ConnectionFromClient.getOutputStream();
					
					BufferedReader headerreader = new BufferedReader(new InputStreamReader(input));
					BufferedWriter headerwriter = new BufferedWriter(new OutputStreamWriter(output));
					
					
					DataOutputStream dataOut = new DataOutputStream(output);
					DataInputStream dataIn = new DataInputStream(input);
					
					String header = headerreader.readLine();
					StringTokenizer strk = new StringTokenizer(header + " ");
					String command = strk.nextToken();
					
					
					if(command.equals("download")) {
						
						try {

							String fileName = strk.nextToken();
							FileInputStream Filein = new FileInputStream("ServerShare/" + fileName);
							int size = Filein.available();
							header = "OK " + size + "\n";
							headerwriter.write(header, 0, header.length());
							headerwriter.flush();
							
							byte[] bytes = new byte[size];
							Filein.read(bytes);
							Filein.close();
							
							dataOut.write(bytes, 0 , size);	
							dataOut.flush();	
							
						}catch (Exception ex) {
							headerwriter.write("NOT FOUND", 0 , "NOT FOUND".length());
							headerwriter.flush();
							
						}finally {
							ConnectionFromClient.close();
						}
						
					}else if(command.equals("check")){
						String fileName = strk.nextToken();
						
						long LastModifinClient = Long.parseLong(strk.nextToken());
						File FileinServer = new File("ServerShare/" + fileName);
						long LastModifinServer = FileinServer.lastModified();
						int Filesize = (int)FileinServer.length();

						if(LastModifinClient > LastModifinServer){
							header = "OK " + Filesize + "\n";
							headerwriter.write(header, 0 ,header.length());
							headerwriter.flush();
							ConnectionFromClient.close();

						}else{
							header = "DIRTY " + Filesize + "\n";
							headerwriter.write(header, 0 ,header.length());
							headerwriter.flush();

							FileInputStream Filein = new FileInputStream(FileinServer);
							byte[] bytes = new byte[Filesize];
							Filein.read(bytes);
							Filein.close();

							dataOut.write(bytes, 0, Filesize);
							dataOut.flush();

							ConnectionFromClient.close();
						}
						

						

					}
					else if(command.equals("upload")) {
						
						try {
							String fileName = strk.nextToken();
							int fileSize = Integer.parseInt(strk.nextToken());
                       

							byte[] space = new byte[fileSize];
	
							dataIn.readFully(space);
	
							try (FileOutputStream fileOut = new FileOutputStream("ServerShare/" + fileName)) {
								fileOut.write(space, 0, fileSize);
								headerwriter.write("Stored\n", 0, "Stored\n".length());
								headerwriter.flush();
								ConnectionFromClient.close();
							}catch(Exception e){
								headerwriter.write("Failed\n", 0, "Failed\n".length());
								headerwriter.flush();
								ConnectionFromClient.close();
							}
						}catch(Exception ex){

						}

						
					}else if(command.equals("list")){
						File folder = new File("ServerShare/");
						File[] listOfFiles = folder.listFiles();

						if( listOfFiles == null || listOfFiles.length == 0){
							headerwriter.write("ERROR\n");
							headerwriter.flush();
						}else{
							StringBuilder fileList = new StringBuilder();
							for( File file : listOfFiles){
								if(file.isFile()){
									fileList.append(file.getName()).append(" ").append(file.length()).append("\n");
								}
							}
							headerwriter.write("OK " + fileList.length() + "\n");
							headerwriter.write(fileList.toString());
							headerwriter.flush();

						}
						
						ConnectionFromClient.close();
						
					}else if(command.equals("resume")){
						try{
							System.out.println("Dekhelt: ");
							String FileName = strk.nextToken();
							String Clientsize = strk.nextToken();
							int sizereceived = Integer.parseInt(Clientsize);
							
							FileInputStream Filein = new FileInputStream("ServerShare/" + FileName);
							int ServerSize = Filein.available();

							int bytesleft = ServerSize - sizereceived;

							System.out.println("Client file size : " + sizereceived);
							System.out.println("Server file size : " + ServerSize);
							System.out.println("Needed Bytes file size : " + bytesleft);
	
							if(bytesleft == 0){
								header = "COMPLETED\n";
								headerwriter.write(header, 0, header.length());
								headerwriter.flush();
								
							}else {

								header ="OK " + bytesleft + "\n";
								headerwriter.write(header, 0 ,header.length());
								headerwriter.flush();
		
								byte[] bytes = new byte[bytesleft];
								
								Filein.read(bytes);
		
								Filein.close();
		
								dataOut.write(bytes, 0, bytesleft);
								dataOut.flush();
							}
							

						}catch( Exception ex6){

						}finally{
							ConnectionFromClient.close();
						}
							
					
					}
					else {
						System.out.println("Wrong Server");
					}
				}
			}
		}catch (Exception ex3) {
			ex3.printStackTrace();
		}
		
		
	}
	

}
