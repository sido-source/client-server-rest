package rest;

import lombok.Builder;
import lombok.Getter;

@Builder
public class CryptoRequest {
    CreditDebitIndicator indicator;
    int val;
    CryptoType cryptoType;
}
