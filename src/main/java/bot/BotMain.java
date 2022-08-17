package bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class BotMain extends TelegramLongPollingBot {

    public static final String BOT_NAME = "school_epta_bot";
    public static final String BOT_TOKEN = "5682624114:AAGtIFToxdc2yWtIMAG-EhjCcDLvSKAS2D4";
    public long user;


    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()&&update.getMessage().hasText()){
            SendMessage sendMessage = new SendMessage();
            sendMessage.setParseMode("HTML");
            sendMessage.setText(messageListener(update.getMessage().getText(),update.getMessage().getFrom().getId()));
            sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }


    public  String messageListener(String message,long user_ud){
        if(message.equals("/start")) {
            return "Привет , даный бот находиться на стадии разработки,идеи писать в /idea (по школьной теме) ";
        }
        if(message.startsWith("/idea")){
            sendMessageTo(String.valueOf(-1001704646260L),
                    "<b>Новая идея от пользователя @"  +user_ud+ " #idea</b>\n<i>" + message+"</i>");
            return "✅ Спасибо, ваша идея на рассмотрении";
        }
        if(message.equals("")){
            return "Привет , даный бот находиться на стадии разработки,";
        }
        return "";
    }
    public boolean sendMessageTo(String chatId, String msg) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setParseMode("HTML");
        sendMessage.setText(msg);
        sendMessage.setChatId(chatId);
        sendMessage.setDisableWebPagePreview(true);
        try {
            execute(sendMessage);
            return true;
        } catch (TelegramApiException e) {
            e.printStackTrace();
            return false;
        }
    }
}
