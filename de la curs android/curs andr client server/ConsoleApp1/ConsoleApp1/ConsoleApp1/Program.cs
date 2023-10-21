using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleApp1
{
    class Program
    {
        static void Main(string[] args)
        {
            TcpListener listener = new TcpListener(IPAddress.Any, 9999);
            Console.WriteLine("Listening...");

            listener.Start();
            TcpClient client = listener.AcceptTcpClient();

            NetworkStream nwStream = client.GetStream();
            byte[] buffer = new byte[client.ReceiveBufferSize];
            int bytesRead = nwStream.Read(buffer, 0, client.ReceiveBufferSize);

            string dataReceived = Encoding.ASCII.GetString(buffer, 0, bytesRead);

            Console.WriteLine("Received: " + dataReceived);

            client.Close();
            listener.Stop();
            Console.ReadLine();
        }
    }
}
