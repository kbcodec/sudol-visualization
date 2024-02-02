package com.sudol.SudolAPI.Services;

import com.sudol.SudolAPI.DTOs.*;
import com.sudol.SudolAPI.Repositories.JordanowskaRepository;
import com.sudol.SudolAPI.Repositories.OpolskaRepository;
import com.sudol.SudolAPI.Repositories.PotoczekRepository;
import com.sudol.SudolAPI.models.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class ExternalDataService {
    private JordanowskaRepository jordanowskaRepository;
    private PotoczekRepository potoczekRepository;
    private OpolskaRepository opolskaRepository;

    ExternalDataService(JordanowskaRepository jordanowskaRepository, PotoczekRepository potoczekRepository, OpolskaRepository opolskaRepository) {
        this.jordanowskaRepository = jordanowskaRepository;
        this.potoczekRepository = potoczekRepository;
        this.opolskaRepository = opolskaRepository;
    }

    @Scheduled(fixedRate = 300000)
    public void updateData() {
        System.out.println("Updating data...");
        try {
            fetchDataAndSave("617101");
            fetchDataAndSave("617103");
            fetchDataAndSave("617107");
            fetchDataAndSave("617093");
            fetchDataAndSave("639606");
            fetchDataAndSave("639607");
            fetchDataAndSave("639615");
            fetchDataAndSave("570456");
            fetchDataAndSave("639619");
            fetchDataAndSave("639621");
            fetchDataAndSave("639622");
        } catch (Exception e) {
            System.err.println("Error during update: " + e.getMessage());
            System.exit(1);
        }

        System.out.println("Data update complete.");
    }

    private void fetchDataAndSave(String endpoint) {
        ExternalApiResponse externalApiResponse = fetchDataFromExternalApi(endpoint);
        saveDataToMongoDB(externalApiResponse, endpoint);
    }

    private ExternalApiResponse fetchDataFromExternalApi(String endpoint) {
        String apiUrl = "https://api.thingspeak.com/channels/" + endpoint + "/feed.json?results=1";
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(apiUrl, ExternalApiResponse.class);
    }

    private void saveDataToMongoDB(ExternalApiResponse externalApiResponse, String endpoint) {
        ChannelModel channel = externalApiResponse.getChannel();
        FeedModel feed = externalApiResponse.getFeeds().get(0);

        String collectionName = CollectionEndpointMap.getCollectionName(endpoint);
        String entryId = String.valueOf(feed.getEntry_id());
        String channelId = String.valueOf(channel.getId());

        if(!documentExists(collectionName, entryId, channelId)) {
            switch (collectionName) {
                case "Jordanowska" -> {
                    JordanowskaDTO jordanowskaDocument = createDocument(channelId, feed, JordanowskaDTO.class);
                    jordanowskaRepository.save(jordanowskaDocument);
                }
                case "Potoczek" -> {
                    PotoczekDTO potoczekDocument = createDocument(channelId, feed, PotoczekDTO.class);
                    potoczekRepository.save(potoczekDocument);
                }
                case "Opolska" -> {
                    OpolskaDTO opolskaDocument = createDocument(channelId, feed, OpolskaDTO.class);
                    opolskaRepository.save(opolskaDocument);
                }
            }
        }
    }

    private <T extends DataDTO> T createDocument(String channelId, FeedModel feed, Class<T> dataType) {
        try {
            T document = dataType.getDeclaredConstructor().newInstance();
            document.setId(feed.getEntry_id());
            document.setChannel_id(channelId);
            String dateExternalFormat = feed.getCreated_at();
            Instant instant = Instant.parse(dateExternalFormat);
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC"));
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
            String outputDate = zonedDateTime.format(outputFormatter);
            document.setDate(outputDate);


            Map<String, String> translations = FieldTransactionMap.getTranslationMap(channelId);

            for (Field field : FeedModel.class.getDeclaredFields()) {
                String fieldName = field.getName();

                if(translations.containsKey(fieldName)) {
                    Field documentField = DataDTO.class.getDeclaredField(translations.get(fieldName));
                    documentField.setAccessible(true);
                    field.setAccessible(true);
                    Object fieldValue = field.get(feed);
                    if (fieldValue != null) {
                        documentField.set(document, fieldValue);
                    }
                }
            }

            return document;

        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }

    }

    private boolean documentExists(String collectionName, String entryId, String channelId) {
        switch (collectionName) {
            case "Jordanowska" -> {
                return jordanowskaRepository.existsByIdAndChannelId(Long.parseLong(entryId), Long.parseLong(channelId));
            }
            case "Potoczek" -> {
                return potoczekRepository.existsByIdAndChannelId(Long.valueOf(entryId), Long.valueOf(channelId));
            }
            case "Opolska" -> {
                return opolskaRepository.existsByIdAndChannelId(Long.valueOf(entryId), Long.valueOf(channelId));
            }
        }
        return true;
    }
}
