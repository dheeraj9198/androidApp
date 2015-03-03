package com.example.dheeraj.superprofs.downloader;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by dheeraj on 27/2/15.
 */
public class HttpLength {
    public static void main(String[] args) throws IOException {
        URL url = new URL("https://s3-ap-southeast-1.amazonaws.com/media.coursehub.tv/102/5643/170744/170744xyz0xyz0.mp4?X-Amz-Date=20150227T141131Z&X-Amz-Expires=300&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Signature=4f9adebbb167da7064be1c324a92e617d73c70d43173f4769eb263bbe4d78ae7&X-Amz-Credential=ASIAJGLE4SGHEDY2VJFQ/20150227/ap-southeast-1/s3/aws4_request&X-Amz-SignedHeaders=Host&x-amz-security-token=AQoDYXdzEC0a0ALm0f6sdcsl5B%2Bmf44I2uTLNx/Bl9w7DGg1HdtisotOeoygDWpiYd3qAS4c/IHwOi68UiPfGcGOBQM1y1MlaUzzGXpp5mtGAmlvD2OoTlqJhqM7K7tTf8407xmdTTzZ2it%2BlCQrW5ZqYbts1AkW5eYd1UxHq7SBjmP4n9kXALqIJ6U8IOHJHn2eNbrFhpx9AcNgEQWRAhvJx1q/msXVLOPwXAkKUlGgGET7GZnHxbODPLka6QOFbco0ujoiglmhaVjHpOB2Z1nq8YpqRaz0hJ87PE%2BiDdB144lVgNdZZsgrI9KsXVhcAPJInmj9TNTalcGDaki65aMG3bOCUPiN9zuotBbcjMyfhsUffeVxNplmHA//id/HFQL%2BNXVlyNOYxu3GU8hJ/chGas1QovhwSEEPQptP%2B%2BeEVwJH9bO6uoez6qWblrUOPW87u%2BUZnPeQV0QgrffApwU%3D");
        URLConnection urlConnection = url.openConnection();
        System.out.println(urlConnection.getContentLength());
        //urlConnection.getInputStream().skip()
    }
}
