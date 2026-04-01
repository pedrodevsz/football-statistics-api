package soccer.api.collector.services.csvDonwload;

import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;

@Service
public class CsvDownloader {

    public InputStream download(String url) throws Exception {
        return new URL(url).openStream();
    }
}

