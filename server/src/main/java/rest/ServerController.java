package rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class ServerController {

    @Autowired
    CryptoService cryptoService;

    @PostMapping(value = "/change", consumes = "application/json")
    void cryptoChangeStatus(CryptoRequest cryptoRequest) {
        cryptoService.changeState(cryptoRequest);
    }

    @GetMapping(value = "/{crypto}", produces = "application/json")
    public CryptoResponse currentStateOfCrypto(@PathVariable("crypto") CryptoType cryptoType) {
        int amount = cryptoService.getCurrentStateOfCrypto(cryptoType);
        return CryptoResponse.builder()
                .cryptoType(cryptoType)
                .val(amount)
                .build();
    }

}
