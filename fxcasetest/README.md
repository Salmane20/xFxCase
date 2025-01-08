# xFx File Transfer Protocol

This project implements a custom file transfer protocol between a client (`xFxClient`) and a server (`xFxServer`). The protocol supports operations such as file download, upload, file listing, and resumption of interrupted transfers. This guide provides details about the protocol, setup, and usage.

## Protocol Overview
The client and server communicate using a custom protocol over a fixed port (`3221`). The client sends commands with necessary arguments (like filenames or file sizes), and the server processes these commands and responds accordingly. The protocol consists of headers for control information and raw data for file contents.

## Supported Commands
### 1. Download
- **Command Syntax:** `download <filename>\n`
- **Client Behavior:**
  - Sends a request to download a file from the server.
  - If the file exists on the server, it downloads the file and saves it in the client's directory.
- **Server Response:**
  - If the file exists: `OK <filesize>\n` followed by file data.
  - If the file is not found: `NOT FOUND\n`.

### 2. Check
- **Command Syntax:** `check <filename> <client_file_size>\n`
- **Client Behavior:**
  - Sends a request to verify if the server file has changed since the last download.
  - Updates the local file if the server indicates it is outdated.
- **Server Response:**
  - If up-to-date: `OK <filesize>\n`.
  - If outdated: `DIRTY <filesize>\n` followed by updated file data.

### 3. Upload
- **Command Syntax:** `upload <filename> <filesize>\n`
- **Client Behavior:**
  - Sends a request to upload a file to the server.
- **Server Response:**
  - On success: `Stored\n`.
  - On failure: `Failed\n`.

### 4. List
- **Command Syntax:** `list\n`
- **Client Behavior:**
  - Requests a list of files available on the server.
- **Server Response:**
  -  If files are available: `OK <list_length>\n` followed by a list in the format:
    ```
    file1 <size>\n
    file2 <size>\n
    ...
    ```
  - If no files: `ERROR\n`.

### 5. Resume
- **Command Syntax:** `resume <filename> <client_file_size>\n`
- **Client Behavior:**
  - Resumes downloading a file from where the transfer was interrupted.
- **Server Response:**
  - If bytes remain: `OK <remaining_bytes>\n` followed by remaining file data.
  - If fully downloaded: `COMPLETED\n`.

## Project Structure
```
Project Directory
│
├── xFxServer.java   # Server-side implementation
├── xFxClient.java   # Client-side implementation
├── ServerShare/     # Directory for server files
└── ClientShare/     # Directory for client files
```

## Setup
1. Compile the Java files:
   ```
   javac xFxServer.java xFxClient.java
   ```

2. Create the required directories for file storage:
   - `ServerShare/` in the project root for server files.
   - `ClientShare/` in the project root for client files.

3. Run the server:
   ```
   java xFxServer
   ```

4. Run the client with a specific command:
   ```
   java xFxClient <command> [filename]
   ```

## Usage Examples
### Download a File
```bash
java xFxClient d <filename>
```

### Upload a File
```bash
java xFxClient u <filename>
```

### List Server Files
```bash
java xFxClient l
```

### Resume File Transfer
```bash
java xFxClient r <filename>
```

### Check File Update
```bash
java xFxClient c <filename>
```

## Notes
- The `ServerShare/` and `ClientShare/` directories must exist prior to running the programs.
- Ensure the port `3221` is not in use by another application.
- Error handling is implemented for scenarios such as missing files, interrupted connections, and invalid commands.

## Limitations and Improvements
- Currently, the server closes the connection after each command. For better performance, consider maintaining persistent connections.
- Enhance error handling for better diagnostics.
- Support for more complex authentication or encryption can be added for secure file transfers.

