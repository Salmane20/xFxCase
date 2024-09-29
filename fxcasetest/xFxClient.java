import java.util.*;
import java.io.*;
import java.net.*;

public class xFxClient {
	
	private static final int PORT = 3221;
	
	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
            System.out.println("Usage: java xfxclient <command> [filename]");
            return;
        }

        String command = args[0];
        String fileName = args.length > 1 ? args[1] : "";
		
		try(Socket ConnectionFromServer = new Socket("localhost",PORT)){
			
			InputStream input = ConnectionFromServer.getInputStream();
			OutputStream output = ConnectionFromServer.getOutputStream();
			
			BufferedReader headerreader = new BufferedReader(new InputStreamReader(input));
			BufferedWriter headerwriter = new BufferedWriter(new OutputStreamWriter(output));
			
			DataInputStream datain = new DataInputStream(input);
			
			if(command.equals("d")) {

				File file = new File("ClientShare/" + fileName);
				int Filesize = (int)file.length();

				if(file.isFile()){
					System.out.println("These File already Exists Lets check if it is Modified ....");

					String header = "check " + fileName + " " + Filesize + "\n";
					headerwriter.write(header, 0 ,header.length());
					headerwriter.flush();

					header = headerreader.readLine();
					StringTokenizer strk = new StringTokenizer(header, " ");
					String status = strk.nextToken();

					if(status.equals("OK")){
						System.out.println("Server Already Up to date !!!");
						
					}else{

						int filesize = Integer.parseInt(strk.nextToken());
						
						byte[] bytes = new byte[filesize];
						datain.readFully(bytes);

						try(FileOutputStream Fileout = new FileOutputStream(file)){
							Fileout.write(bytes, 0, filesize);
						}

						System.out.println("The file has been Updated !!");

					}

				}else{

					String header = "download " + fileName +"\n";
					headerwriter.write(header, 0 ,header.length());
					headerwriter.flush();
					
					header = headerreader.readLine();
					
					
					if(header.equals("NOT FOUND")) {
						System.out.println("File not Found");
					}else {
						
						StringTokenizer strk = new StringTokenizer(header, " ");
						String status = strk.nextToken();
						
						
						
						if(status.equals("OK")) {
							
							String size = strk.nextToken();
							int space = Integer.parseInt(size);
							
							byte[] bytes = new byte[space];
							
							datain.readFully(bytes);
							
							try(FileOutputStream Fileout = new FileOutputStream("ClientShare/" + fileName)) {
								Fileout.write(bytes, 0, space);	
								
							}
							
						}else {
							System.out.println("You are not connected to the Right Server");
						}
			
						
					}
				}

			}else if(command.equals("u")) {
				try {

                    DataOutputStream dataOut = new DataOutputStream(output);

                    FileInputStream fileIn = new FileInputStream("ClientShare/" + fileName);

                    int fileSize = fileIn.available();

                    String header = "upload " + fileName + " " + fileSize + "\n";

                    headerwriter.write(header, 0, header.length());
                    headerwriter.flush();
                    
                    byte[] bytes = new byte[fileSize];
                    fileIn.read(bytes);

                    fileIn.close();

                    dataOut.write(bytes, 0, fileSize);

                    header = headerreader.readLine();

                    if (header.equals("Failed")) {
                        System.out.println(header);
                    } else {
                        System.out.println(header);
                    }
                } catch (Exception e) {

                }


			}else if(command.equals("l")){

					String header = "list\n";
					headerwriter.write(header , 0 , header.length());
					headerwriter.flush();

					String resheader = headerreader.readLine();
					if( resheader.startsWith("OK")){
						int Listlength = Integer.parseInt(resheader.split(" ")[1]);
						char[] Files = new char[Listlength];
						headerreader.read(Files, 0,Listlength);
						System.out.println("Available Files : ");
						System.out.println(new String(Files));
						
					}else {
						System.out.println("Doesn't work");
					}

				
			}else if(command.equals("r")){
				
					FileInputStream Filein = new FileInputStream("ClientShare/" + fileName);
					int filesize = Filein.available();

					String header = "resume " + fileName + " " + filesize + "\n";
					headerwriter.write(header, 0, header.length());
					headerwriter.flush();

					Filein.close();

					String resheader = headerreader.readLine();
					StringTokenizer strk1 = new StringTokenizer(resheader, " ");
					String status = strk1.nextToken();
					String neededbytes = strk1.nextToken();
					int bytesleft = Integer.parseInt(neededbytes);

					if( status.equals("OK")){

						byte[] bytes = new byte[bytesleft];
						datain.readFully(bytes);

						try(FileOutputStream Fileout = new FileOutputStream("Clientshare/" + fileName)){
							Fileout.write(bytes, 0 , neededbytes.length());
						}


					}else{
						System.out.println("Completed Downloading");
					}




				
			}
		}
		
	}

}



