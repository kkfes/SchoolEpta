package bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class BotMain extends TelegramLongPollingBot {

    public static final String BOT_NAME = "scholotron_bot";
    public static final String BOT_TOKEN = "5478245744:AAGItaRwXXMfIpdYezpmLbosLIfZBXf2mUs";


    @Override
    public String getBotUsername() {
        return null;
    }

    @Override
    public String getBotToken() {
        return null;
    }

    @Override
    public void onUpdateReceived(Update update) {

    }
}
