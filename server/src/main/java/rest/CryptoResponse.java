package rest;

import lombok.Builder;

@Builder
public class CryptoResponse {
    int val;
    CryptoType cryptoType;
}
