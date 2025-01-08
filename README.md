# xFx Application

The xFx Application is a Java-based client-server application designed for efficient file sharing. It contains two main folders:

- **ClientShare**
- **ServerShare**

Each folder includes files that can be manipulated as per the application's functionality.

## Features

### 1. Downloading a File
To download a file from the server, use the following command after compiling:
```
java xfxServer d {FileName}
```
**Note**: The server automatically checks if the file is "Okay" or "Dirty" and returns the corresponding header protocol.

### 2. Uploading a File
To upload a file to the server, use the following command after compiling:
```
java xfxServer u {FileName}
```

### 3. Listing Files on the Server
To list all files available in the `ServerShare` folder, use the following command after compiling:
```
java xfxServer l
```

### 4. Resuming a Download
To resume downloading a file, use the following command after compiling:
```
java xfxServer r {FileName}
```

## Additional Information
- Ensure all files are properly placed within their respective folders (`ClientShare` and `ServerShare`).
- The application uses a robust file integrity check to ensure data consistency during downloads.

**PS**: This file sharing application simplifies remote file management with a clean and efficient command-line interface.

