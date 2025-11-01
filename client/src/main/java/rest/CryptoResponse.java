package rest;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CryptoResponse {
    int val;
    CryptoType cryptoType;
}
