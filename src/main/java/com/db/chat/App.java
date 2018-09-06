package com.db.chat;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        History history = new History();
        Server server = new Server(history);

        System.out.println( "Hello World!" );
    }
}
