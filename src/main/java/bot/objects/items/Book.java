package bot.objects.items;

import bot.BotMain;
import bot.objects.Users;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Book extends Item{

    public Book(long id,String emoji,String text,int cost,boolean shop) {
        this.id=id;
        this.emoji=emoji;
        this.text=text;
        this.cost=cost;
        this.shop=shop;
    }

    public void use(long id,long chat_id){
        Users users = Users.get(id);
        users.setLesson(users.getLesson()-180);
        users.save();
        new BotMain().sendMessageTo(String.valueOf(chat_id),"\uD83D\uDCD5 Ты полистал книгу про Рофлы и нихуя не понял, зато ты вернешься на 3 минуты раньше!");
        SendSticker sendSticker = new SendSticker();
        sendSticker.setSticker(new InputFile("CAACAgIAAxkBAAHuKVljB3UX-RvZEHryB04MnZTNO-lpbQACZQADFlb4KFnEtmeqU6_UKQQ"));
        sendSticker.setChatId(String.valueOf(chat_id));
        try {
            new BotMain().execute(sendSticker);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
