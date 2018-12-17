#include <stdio.h>
#include <stdlib.h>
#include <netinet/in.h>
#include <fcntl.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <string.h>
#include<signal.h>

#define PORT 6789


//listen to port (attempt) and get socket id, -1 if error
int newSocketToPort(int port, int maxConnReqBacklog) {

	//create socket
	int serverSocket = socket (AF_INET, SOCK_STREAM, 0); //SOCK_STREAM, SOCK_SEQPACKET
	if (!(serverSocket >= 0)) {
		//error opening socket
		return -1;
	}

	//set non blocking mode
	fcntl(serverSocket, F_SETFL, O_NONBLOCK);

	//bind socket to listening port
	struct sockaddr_in serverAddress;
	memset(&serverAddress, 0, sizeof(serverAddress));
	serverAddress.sin_family      = AF_INET;
	serverAddress.sin_addr.s_addr = inet_addr("127.0.0.1");
	serverAddress.sin_port        = htons (port);

	//let the server start immediately after a previous shutdown
	int optionValue = 1;
	setsockopt(serverSocket, SOL_SOCKET, SO_REUSEADDR, &optionValue, sizeof(int));

	int bindSuccess = bind(serverSocket, (struct sockaddr*)&serverAddress, sizeof(serverAddress));

	if (!(bindSuccess >= 0)) {
		//error binding socket to port
		return -1;
	}

	listen(serverSocket, maxConnReqBacklog);

	return serverSocket;
}


//close an open socket
void closeSocket(int socket) {
	close(socket);
}


//accept a connection on a given socket id (if one is avaliable to accept)
int acceptConnection(int serverSocket) {

	// accept the connection
	struct sockaddr_in clientAddress;
	memset(&clientAddress, 0, sizeof(clientAddress));
	socklen_t clientLen = sizeof(clientAddress);
	int connectionSocket = accept(serverSocket, (struct sockaddr*)&clientAddress, &clientLen);

	if (connectionSocket != -1) {

		//set non blocking mode
		fcntl(connectionSocket, F_SETFL, O_NONBLOCK);

        //ignore sigpipes for dead sender
        signal(SIGPIPE,SIG_IGN);

		//string strAddress(inet_ntoa(clientAddress.sin_addr));
		printf("Connected with %s\n", inet_ntoa(clientAddress.sin_addr));
		return connectionSocket;
	}

	return -1;

}


//send data to a socket (garenteed to be received in order)
void socketSend(int socket, void* data, long length) {
	send(socket, data, length, 0);
}

void socketSendString(int socket, char *str) {
	//printf ("%s\n", str);
    socketSend(socket, str, strlen(str));
	socketSend(socket, "\n", strlen("\n"));
}

//receive data from a socket (will receive nothing if data not avaliable)
long socketRecv(int socket, void* buffer, long maxLen) {
	return recv(socket, buffer, maxLen, 0);
}


void MicroSleep(int n) {
	struct timeval tm;
	unsigned long seconds = n/1000;
	unsigned long useconds = n%1000;
	if (useconds > 1000) { useconds -= 1000; seconds++; }
	useconds*=1000; // using usec
	tm.tv_sec = seconds;
	tm.tv_usec = useconds;
	select(0, 0, 0, 0, &tm);
}


int systemCommand(int client, char *cmd, int outputToClient) {
	FILE *fp;
	char path[1035];
	int input = 0;
	/* Open the command for reading. */
	fp = popen(cmd, "r");
	if (fp != NULL) {

	    /* Read the output a line at a time - output it. */
	    while (fgets(path, sizeof(path)-1, fp) != NULL) {
		    int lineInputLength = strlen(path);
            input += lineInputLength;
		    if (outputToClient) {
                path[lineInputLength-1] = 0;
		    	socketSendString(client, "1");
		    	socketSendString(client, path);
		    }
	    }

	    /* close */
	    pclose(fp);
    }
    	
	if (outputToClient) 
		socketSendString(client, "END");

	return input;
}


void command(int client, char *cmd) {
    //printf("Received command: %s\n", cmd);
	systemCommand(client, cmd, 0==0);
}


#define BUF_LEN 1024

void serveConsole(int socket) {
	printf("Serving System Command Proxy...\n");

	int client = -1;

	int len = 0;
	char *buf = (char *)malloc(BUF_LEN);

#define TEMP_BUF_LEN 256
	char *tempBuf = (char *)malloc(TEMP_BUF_LEN);

	while (0==0) {

		int newClient = acceptConnection(socket);

		if (newClient != -1) {
			if (client != -1) {
				//socketSendString(client, "Some noob stole your session.");
				closeSocket(client);
			}
			client = newClient;
			printf("Serving new client.\n");
			//socketSendString(client, "You have connected to a noob server.");
		}

		if (client != -1) {
			int count = socketRecv(client, tempBuf, TEMP_BUF_LEN);
			int i;
			for (i = 0; i < count; ++i) {
				char c = tempBuf[i];
				if (c > 0) {
					if (len >= BUF_LEN-1) {
						len = 0;
						//socketSendString(client, "Command was too long.");
					} else {
						if (len == -1) {
							if (c == '/') {
								len = 0;
							}
						} else {
							if (c == '\n') {
								buf[len] = 0;
								len = 0;
								command(client, buf);
							} else {
								buf[len] = c;
								len++;
							}
						}
					}
				}
			}				
		}

		MicroSleep(400);
	}

	free(tempBuf);
	free(buf);
}

int main() {

	printf("Starting System Command Proxy...\n");

	int socket = newSocketToPort(PORT, 15);
	if (socket == -1) {
		printf("Socket binding error on port: %d\n", PORT);
	} else {
		serveConsole(socket);
		closeSocket(socket);
	}

	return 0;
}


