import Cloud.Client.ClientHandler;
import Cloud.Client.Controller;
import Cloud.Client.NettyClient;
import Cloud.Server.ServerMain;
import org.junit.Assert;
import org.junit.Test;

public class ServerTest {
    ServerMain serverMain;

    @Test
    public void testADD(){
    serverMain = new ServerMain();


        Thread t = new Thread(()->{
            try {
                NettyClient.ClientRun(new Controller());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.setDaemon(true);
        t.start();

        try {
            Thread.sleep(1000); // Немного ждем запуска NettyClient
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

       ClientHandler clientHandler = NettyClient.getClientHandler();

        try {
            serverMain.run();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

}
