package bot.objects;

import bot.DBManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

public class Users {
    private long id;
    private String name;
    private String user;
    private int balance;
    private int knowledge;
    private int experience;
    private long lesson;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(int knowledge) {
        this.knowledge = knowledge;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public long getLesson() {
        return lesson;
    }

    public void setLesson(long lesson) {
        this.lesson = lesson;
    }

    public static void add(long id,String name,String user){
        if(get(id)!=null){
            return;
        }
        try {
            PreparedStatement statement = DBManager.connection.prepareStatement("INSERT INTO `users`(`id`, `name`, `user`) VALUES ('"+id+"','"+name+"','"+user+"')");
            statement.executeUpdate();
            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Users get(long id){
        Users users = null;
        try {
            PreparedStatement statement = DBManager.connection.prepareStatement("SELECT * FROM users WHERE id = ?");
            statement.setLong(1,id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                users=new Users();
                users.setId(resultSet.getLong("id"));
                users.setName(resultSet.getString("name"));
                users.setUser(resultSet.getString("user"));
                users.setBalance(resultSet.getInt("balance"));
                users.setKnowledge(resultSet.getInt("knowledge"));
                users.setExperience(resultSet.getInt("experience"));
                users.setLesson(resultSet.getLong("lesson"));
            }
            statement.close();
        }catch (Exception e){
            e.printStackTrace();
            try {
                {
                    PreparedStatement statement = DBManager.connection.prepareStatement("CREATE TABLE `" + DBManager.DB + "`.`users` ( `id` VARCHAR(255) NOT NULL , `name` VARCHAR(255) NOT NULL , `user` VARCHAR(255) NULL DEFAULT NULL , `balance` INT(11) NOT NULL DEFAULT '0' , `knowledge` INT(11) NOT NULL DEFAULT '0' , `experience` INT(11) NOT NULL DEFAULT '0' , `lesson` INT(11) NOT NULL DEFAULT '0' ) ENGINE = InnoDB;");
                    statement.executeUpdate();
                    statement.close();
                }
                {
                    PreparedStatement statement = DBManager.connection.prepareStatement("ALTER TABLE `users` ADD UNIQUE(`id`);");
                    statement.executeUpdate();
                    statement.close();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return users;
    }

    public void save(){
        try {
            PreparedStatement statement = DBManager.connection.prepareStatement("UPDATE `users` SET `name`='"+this.name+"',`user`='"+this.user+"',`balance`='"+this.balance+"',`knowledge`='"+this.knowledge+"',`experience`='"+this.experience+"',`lesson`='"+this.lesson+"' WHERE `id`='"+this.id+"'");
            statement.executeUpdate();
            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String link(){
        if(this.user==null){
            return "<a href='tg://openmessage?user_id=/"+this.id+"'>"+this.name+"</a>";
        }else {
            return "<a href='https://t.me/"+this.user+"'>"+this.name+"</a>";
        }
    }

    public String getProfileExperience(){
        int i = (this.knowledge+1)*50;
        int k = this.knowledge;
        int how = k*10/i;
        String text = "";
        int prc = k*100/i;
        for (int j = 0; j < 10; j++) {
            if(how!=0){
                text+="▓";
                how--;
            }else {
                text+="░";
            }
        }
        return "<code>"+text+"</code> ("+prc+"%)";
    }

    public static String profile(long id){
        Users users = get(id);
        if(users==null){
            return "⚠️ ERROR: You don't exist in this life.";
        }
        String text = "<b>\uD83D\uDCD6 Профиль ученика "+users.link()+"\n" +
                "\uD83C\uDF15 Монет:</b> <code>"+users.balance+"</code>\n" +
                "<b>\uD83D\uDCD0 УР. Знания:</b> <code>"+users.knowledge+"</code>\n" +
                users.getProfileExperience()+"\n"+
                (users.lesson==0?"\uD83D\uDE1D Бездельничает":"✅ На занятиях");
        return text;
    }

    public static String goToSchool(long id){
        Users users = get(id);
        if(users==null){
            return "⚠️ ERROR: You don't exist in this life.";
        }
        if(users.lesson==0){
            users.setLesson(System.currentTimeMillis()/1000+10800);
            users.save();
            return "\uD83D\uDEB6\u200D♂️ Ты пошел в школу, зачем? А главное нахуя?";
        }else {
            if (System.currentTimeMillis() / 1000 >= users.lesson) {
                int exp = new Random().nextInt(5);
                int bal = new Random().nextInt(10);
                users.setExperience(users.getExperience()+exp);
                users.setBalance(users.getBalance()+bal);
                users.save();
                return "\uD83D\uDE15 Ты весь измотанный пришел со школе домой, А СТОП, у тебя нет дома, ты пришел на свалку.\n" +
                        "+"+bal+" \uD83C\uDF15 монет\n" +
                        "+"+exp+" \uD83D\uDD06 опыт";
            } else {
                return "Ты как бы уже в школе, не спи и учи уроки, вернешься через " + ((users.lesson - System.currentTimeMillis() / 1000)) / 60 + " мин";
            }
        }
    }

}
