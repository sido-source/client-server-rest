package rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ClientMain {

    public static void main(String[] args) {

        ConfigurableApplicationContext ctx = SpringApplication.run(ClientMain.class);
        ClientService bean = ctx.getBean(ClientService.class);
        CryptoResponse cryptoResponse = bean.getCurrent(CryptoType.BTC);
        System.out.println(cryptoResponse);
        bean.sendChange();
        System.out.println(" changed btc ");
        cryptoResponse = bean.getCurrent(CryptoType.BTC);
        System.out.println(cryptoResponse);

    }
}
