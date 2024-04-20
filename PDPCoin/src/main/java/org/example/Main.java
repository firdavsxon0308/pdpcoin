package org.example;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new MyBot("6303329269:AAHFe6eT2yuk23u8SaOrw_kstlRoL4565Fg"));
        }catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}