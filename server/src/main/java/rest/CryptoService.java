package rest;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CryptoService {
    Map<CryptoType, Integer> cryptoWithAmount;
    Map<CryptoType, CryptoRequest> historyOfChanges;

    public CryptoService() {
        cryptoWithAmount = new HashMap<>();
        cryptoWithAmount.put(CryptoType.ETH, 10);
        cryptoWithAmount.put(CryptoType.BTC, 200);
        cryptoWithAmount.put(CryptoType.XRP, 345);
    }

    void changeState(CryptoRequest cryptoRequest) {
        CreditDebitIndicator indicator = cryptoRequest.getIndicator();
        Integer amount = cryptoWithAmount.get(cryptoRequest.getCryptoType());
        CryptoType cryptoType = cryptoRequest.cryptoType;

        if (indicator.equals(CreditDebitIndicator.credit)) {
            amount += cryptoRequest.val;
            cryptoWithAmount.put(cryptoType, amount);
        } else {
            amount -= cryptoRequest.val;
            cryptoWithAmount.put(cryptoType, amount);
        }

        historyOfChanges.put(cryptoType, cryptoRequest);
    }

    int getCurrentStateOfCrypto(CryptoType cryptoType) {
        return cryptoWithAmount.get(cryptoType);
    }
}
