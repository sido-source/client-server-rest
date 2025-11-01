package rest;

import lombok.Getter;

@Getter
public class CryptoRequest {
    CreditDebitIndicator indicator;
    int val;
    CryptoType cryptoType;
}
