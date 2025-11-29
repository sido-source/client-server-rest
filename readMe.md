public class HttpEntity<T> {
public static final HttpEntity<?> EMPTY;
private final HttpHeaders headers;
@Nullable
private final T body;

    protected HttpEntity() {
        this((Object)null, (MultiValueMap)null);
    }

    public HttpEntity(T body) {
        this(body, (MultiValueMap)null);
    }

    public HttpEntity(MultiValueMap<String, String> headers) {
        this((Object)null, headers);
    }



sending post
@Nullable
protected <T> T doExecute(URI url, @Nullable String uriTemplate, @Nullable HttpMethod method, @Nullable RequestCallback requestCallback, @Nullable ResponseExtractor<T> responseExtractor) throws RestClientException {
Assert.notNull(url, "url is required");
Assert.notNull(method, "HttpMethod is required");

        ClientHttpRequest request;
        try {
            request = this.createRequest(url, method);
        } catch (IOException var21) {
            IOException ex = var21;
            throw createResourceAccessException(url, method, ex);
        }

        ClientRequestObservationContext observationContext = new ClientRequestObservationContext(request);
        observationContext.setUriTemplate(uriTemplate);
        Observation observation = ClientHttpObservationDocumentation.HTTP_CLIENT_EXCHANGES.observation(this.observationConvention, DEFAULT_OBSERVATION_CONVENTION, () -> {
            return observationContext;
        }, this.observationRegistry).start();




exception during "restTemplate.postForLocation" 

Exception in thread "main" org.springframework.web.client.ResourceAccessException: I/O error on POST request for "http://localhost/api/v1/change": Connection refused
at org.springframework.web.client.RestTemplate.createResourceAccessException(RestTemplate.java:926)
at org.springframework.web.client.RestTemplate.doExecute(RestTemplate.java:906)
at org.springframework.web.client.RestTemplate.execute(RestTemplate.java:801)
at org.springframework.web.client.RestTemplate.postForLocation(RestTemplate.java:488)
at rest.ClientService.sendChange(ClientService.java:21)
at rest.Client.main(Client.java:15)
Caused by: java.net.ConnectException: Connection refused
at java.base/sun.nio.ch.Net.pollConnect(Native Method)
at java.base/sun.nio.ch.Net.pollConnectNow(Net.java:682)
at java.base/sun.nio.ch.NioSocketImpl.timedFinishConnect(NioSocketImpl.java:542)
at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:592)
at java.base/java.net.Socket.connect(Socket.java:751)
at java.base/sun.net.NetworkClient.doConnect(NetworkClient.java:178)
at java.base/sun.net.www.http.HttpClient.openServer(HttpClient.java:531)
at java.base/sun.net.www.http.HttpClient.openServer(HttpClient.java:636)
at java.base/sun.net.www.http.HttpClient.<init>(HttpClient.java:280)
at java.base/sun.net.www.http.HttpClient.New(HttpClient.java:386)
at java.base/sun.net.www.http.HttpClient.New(HttpClient.java:408)
at java.base/sun.net.www.protocol.http.HttpURLConnection.getNewHttpClient(HttpURLConnection.java:1304)
at java.base/sun.net.www.protocol.http.HttpURLConnection.plainConnect0(HttpURLConnection.java:1237)
at java.base/sun.net.www.protocol.http.HttpURLConnection.plainConnect(HttpURLConnection.java:1123)
at java.base/sun.net.www.protocol.http.HttpURLConnection.connect(HttpURLConnection.java:1052)
at org.springframework.http.client.SimpleClientHttpRequest.executeInternal(SimpleClientHttpRequest.java:79)
at org.springframework.http.client.AbstractStreamingClientHttpRequest.executeInternal(AbstractStreamingClientHttpRequest.java:71)
at org.springframework.http.client.AbstractClientHttpRequest.execute(AbstractClientHttpRequest.java:81)
at org.springframework.web.client.RestTemplate.doExecute(RestTemplate.java:900)