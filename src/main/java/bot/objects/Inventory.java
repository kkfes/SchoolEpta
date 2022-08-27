package bot.objects;

import bot.BotMain;
import bot.DBManager;
import bot.objects.items.Book;
import bot.objects.items.Books;
import bot.objects.items.Item;
import bot.objects.items.Gosha;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Inventory {
    public static ArrayList<Item> ids = new ArrayList<>();
    static {
        try {
            PreparedStatement statement = DBManager.connection.prepareStatement("create table items\n" +
                    "(\n" +
                    "    user_id    varchar(255) null,\n" +
                    "    item_id    int          null,\n" +
                    "    item_count int          null\n" +
                    ")");
            statement.executeUpdate();
            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        ids.add(new Book(0,"\uD83D\uDCD3","Книга, уменьшает время в школе на 3 минуты",1,true));
        ids.add(new Books(1,"\uD83D\uDCDA","Учебники, повышает процент знаний в два раза",1,true));
        ids.add(new Gosha(2,"\uD83E\uDEB4","Это гоша дальше хз",1,true));
    }

    public static void useItem(String emoji,long user_id,long chat_id,long reply){
        Item item = getItemId(emoji);
        Users u = Users.get(user_id);
        if(item==null){
            new BotMain().sendMessageTo(String.valueOf(chat_id),"❎ Такого предмета не существует");
        }else {
            if(u.getLesson()==0){
                new BotMain().sendMessageTo(String.valueOf(chat_id),"❎ Ты же не в школе, бешом в школу");
                return;
            }
            if(getItem(user_id,item.id,1)){
                if(item.id==0){
                    Book book = (Book) item;
                    book.use(user_id,chat_id);
                }else if(item.id==1){
                    Books book = (Books) item;
                    book.use(user_id,chat_id);
                }
            }else {
                new BotMain().sendMessageTo(String.valueOf(chat_id),"❎ У вас нет такого предмета");
            }
        }
    }


    private long user_id;
    private long item_id;
    private int item_count;

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getItem_id() {
        return item_id;
    }

    public void setItem_id(long item_id) {
        this.item_id = item_id;
    }

    public int getItem_count() {
        return item_count;
    }

    public void setItem_count(int item_count) {
        this.item_count = item_count;
    }

    public static void addItem(long user_id,long item_id,int how){
        Inventory inventory = getItem(user_id, item_id);
        if(inventory!=null){
            try {
                PreparedStatement statement = DBManager.connection.prepareStatement("UPDATE items SET item_count = ? WHERE user_id = ? && item_id = ?");
                statement.setLong(2,user_id);
                statement.setLong(3,item_id);
                statement.setInt(1,inventory.item_count+how);
                statement.executeUpdate();
                statement.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            try {
                PreparedStatement statement = DBManager.connection.prepareStatement("INSERT INTO items(user_id, item_id, item_count) VALUE (?,?,?)");
                statement.setLong(1,user_id);
                statement.setLong(2,item_id);
                statement.setInt(3,how);
                statement.executeUpdate();
                statement.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static boolean hastem(long user_id,long item_id){
        Inventory inventory = getItem(user_id, item_id);
        if(inventory!=null){
            return true;
        }else {
            return false;
        }
    }


    public static boolean getItem(long user_id,long item_id,int how){
        Inventory inventory = getItem(user_id, item_id);
        if(inventory!=null){
            if(inventory.item_count>=how){
                try {
                    PreparedStatement statement = DBManager.connection.prepareStatement("UPDATE items SET item_count = ? WHERE user_id = ? && item_id = ?");
                    statement.setLong(2,user_id);
                    statement.setLong(3,item_id);
                    statement.setInt(1,inventory.item_count-how);
                    statement.executeUpdate();
                    statement.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
                Inventory i = getItem(user_id, item_id);
                if(i.item_count==0){
                    try {
                        PreparedStatement statement = DBManager.connection.prepareStatement("DELETE FROM items WHERE item_id = ? && user_id = ?");
                        statement.setLong(1,item_id);
                        statement.setLong(2,user_id);
                        statement.executeUpdate();
                        statement.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

    public static Inventory getItem(long user_id,long item_id){
        Inventory inventory = null;
        try {
            PreparedStatement statement = DBManager.connection.prepareStatement("SELECT * FROM items WHERE  user_id = ? && item_id = ?");
            statement.setLong(1,user_id);
            statement.setLong(2,item_id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                inventory=new Inventory();
                inventory.setItem_id(resultSet.getInt("item_id"));
                inventory.setUser_id(resultSet.getLong("user_id"));
                inventory.setItem_count(resultSet.getInt("item_count"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return inventory;
    }

    public static Item getItem(long id){
        for(Item i:ids){
            if(i.id==id){
                return i;
            }
        }
        return null;
    }


    public static Item getItemId(String emoji){
        for(Item i:ids){
            if(i.emoji.equalsIgnoreCase(emoji.trim())){
                return i;
            }
        }
        return null;
    }

    public static void buyItem(String emoji,long user_id,long chat_id,int how){
        Item item = getItemId(emoji);
        Users u = Users.get(user_id);
        if(item==null){
            new BotMain().sendMessageTo(String.valueOf(chat_id),"❎ Такого предмета не существует");
        }else {
            if(item.shop) {
                int cost = item.cost * how;
                if (u.getBalance() >= cost) {
                    addItem(user_id, item.id, how);
                    u.setBalance(u.getBalance() - cost);
                    u.save();
                    new BotMain().sendMessageTo(String.valueOf(chat_id), "✅ Вы купили " + item.emoji + " " + how);
                } else {
                    new BotMain().sendMessageTo(String.valueOf(chat_id), "❎ Не хватает монет для покупки");
                }
            }else {
                new BotMain().sendMessageTo(String.valueOf(chat_id), "❎ Этот товар нельзя купить");
            }
        }
    }

    public static void give(long id1,long id2,long chat_id,String emoji,int how){
        Item item = getItemId(emoji);
        Users u = Users.get(id1);
        if(item==null){
            new BotMain().sendMessageTo(String.valueOf(chat_id),"❎ Такого предмета не существует");
        }else {
            if(getItem(id1,item.id,how)){
                addItem(id2,item.id,how);
                new BotMain().sendMessageTo(String.valueOf(chat_id),"✅ Передано");
            }else {
                new BotMain().sendMessageTo(String.valueOf(chat_id),"❎ Не хватает");
            }
        }
    }

    public static String shop(){
        String text = "Буфет\n";
        for (Item i:ids){
            if(i.shop){
                text+=i.emoji+" "+i.text+" "+i.cost;
            }
        }
        return text;
    }

    public static String getInventory(long user_id){
        String text = "\uD83C\uDF92 Ваш портфель:";
        try {
            PreparedStatement statement = DBManager.connection.prepareStatement("SELECT * FROM items WHERE user_id = ?");
            statement.setLong(1,user_id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                Item item = getItem(resultSet.getLong("item_id"));
                if(item!=null){
                    text+="\n • "+resultSet.getString("item_count")+" <code>"+item.emoji+"</code> "+item.text;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        text+="\n\n❔ Ты можешь нажать на эмодзи предмета и скопировать его";
        return text;
    }



}
