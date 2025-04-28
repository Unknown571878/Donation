package com.example.donation;

/*import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;

public class FcmPush {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String url = "https://fcm.googleapis.com/fcm/send";
    private String serverKey = "APIkey";
    private Gson gson;
    private OkHttpClient okHttpClient;

    public static FcmPush instance = new FcmPush();

    private FcmPush() {
        gson = new Gson();
        okHttpClient = new OkHttpClient();
    }

    public void sendMessage(final String destinationUid, final String title, final String message) {
        FirebaseFirestore.getInstance().collection("pushtokens").document(destinationUid).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String token = task.getResult().getString("pushToken");

                        if (token != null) {
                            sendPushMessage(token, title, message);
                        } else {
                            // Handle the case where pushToken is null
                        }
                    } else {
                        // Handle the case where Firestore data retrieval fails
                    }
                });
    }

    private void sendPushMessage(String token, String title, String message) {
        PushDTO pushDTO = createPushDTO(token, title, message);

        RequestBody body = RequestBody.create(JSON, gson.toJson(pushDTO));
        Request request = new Request.Builder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "key=" + serverKey)
                .url(url)
                .post(body)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle the case where the network call fails
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle the response from FCM server
                System.out.println(response.body().string());
            }
        });
    }

    private PushDTO createPushDTO(String token, String title, String message) {
        PushDTO pushDTO = new PushDTO();
        pushDTO.setTo(token);
        pushDTO.getNotification().setTitle(title);
        pushDTO.getNotification().setBody(message);
        return pushDTO;
    }
}*/
