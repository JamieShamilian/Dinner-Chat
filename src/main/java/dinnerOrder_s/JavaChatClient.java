/*
 */
package dinnerOrder_s;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JRadioButton;
import javax.swing.JButton;




/**
 *
 * @author jamie
 */
public class JavaChatClient {

    BufferedReader in;
    PrintWriter out;

    JFrame frame;
    JTextField textField;
    JTextArea messageArea,messageArea2;
    JToggleButton b1,b2,b3,b4,b5,b6,b7,b8,b9,b0,debug;
    JRadioButton r1,r2,r3,r0;
    JButton place,cancel,refresh;
    String Order;
    
    ArrayList<JToggleButton> buttons = new ArrayList<>(); 
    ArrayList<JRadioButton> radios = new ArrayList<>(); 
  
    
    String dinnerType=null;
    int dinnerIndex = 0;
    
public class current {
    String name;
    String[] size ;
    String[] sizeDone ;
    
}     
    
static class pizza {
    static String[] size = {"Small","Medium","Large"};
    static String[] sizeDone = { "Pizza Size Small, ",
         "Pizza Size Medium, ",
         "Pizza Size Large, " };
    static String b1 = "Pepperoni";
    static String b2 = "Mushrooms";
    static String b3 = "Onions";
    static String b4 = "Sausage";
    static String b5 = "Bacon";
    static String b6 = "Black Olives";
    static String b7 = "Green Peppers";
    static String b8 = "Extra Cheese";
    static String b9 = "Meatballs";
    
    static String r1 = "Small";
    static String r2 = "Medium";
    static String r3 = "Large";                            
            
}

static class hero {
    static String[] size = {"mini","half","whole"};
    static String[] sizeDone = { "Hero Size mini, ",
         "Hero Size half, ",
         "Hero Size whole, " };
        
    static String b1 = "Pepperoni";
    static String b2 = "Ham";
    static String b3 = "Salami";
    static String b4 = "Roast Beef";
    static String b5 = "Bacon";
    static String b6 = "Black Olives";
    static String b7 = "Green Peppers";
    static String b8 = "Lettuce";
    static String b9 = "Tomato";
    
    static String r1 = "Mini";
    static String r2 = "Half";
    static String r3 = "Whole";                            
             
}


    
    /**
     * Constructs the client by laying out the GUI and registering a
     * listener with the textfield so that pressing Return in the
     * listener sends the textfield contents to the server.  Note
     * however that the textfield is initially NOT editable, and
     * only becomes editable AFTER the client receives the NAMEACCEPTED
     * message from the server.
     */
    public JavaChatClient() {
    }
    
     private void sendStatus()
        {
            
    
            
        for ( JRadioButton r : radios ) {
            if ( r.isSelected() )
                out.println("^+"+r.getText());
            else
                out.println("^-"+r.getText());
        }
            
        
                
        for ( JToggleButton b : buttons ) {
            if ( b.isSelected() )
                out.println("+"+b.getText());
            else
                out.println("-"+b.getText());
        }
    }
        
    
    public void setup()
    {

        // Add Listeners
        textField.addActionListener(new ActionListener() {
            /**
             * Responds to pressing the enter key in the textfield by sending
             * the contents of the text field to the server.    Then clear
             * the text area in preparation for the next message.
             */
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
                textField.setText("");
            }
        });
        
        place.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                Order = new String("No Size Selected ");
                
                for ( JRadioButton r : radios ) {
                    if ( r.isSelected() ) {
                        Order = ( r.getText() + ", ") ;
                        break;
                    }
                }
                
                for ( JToggleButton b : buttons ) {
                    if ( b.isSelected() )
                        Order += ( b.getText() + ", ") ;
                }
                
                messageArea2.append (Order+"\n");      
                
                out.println("Placed Order:" + Order);

            }
        });
        
        
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Order = "Order Canceled";
                messageArea2.append (Order+"\n");
 
                
                for ( JRadioButton r : radios ) {
                    r.setSelected(false);
                 }
            
                for ( JToggleButton b : buttons ) {
                    b.setSelected(false);
                }
                
                sendStatus();
                
            }
        });
        
       
        
        refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendStatus();
            }
        });
   
        /*
        for ( JRadioButton r : radios ) {
            
            if ( r.isSelected() )
                Order += ( r.getText() + ", ") ;
        }
        
        */
        
        for ( JToggleButton b : buttons ) {
            
            b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if ( b.isSelected() )
                     out.println("+"+b.getText());
                    else
                     out.println("-"+b.getText());
                }
            });
        }
        

        
         r1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ( r1.isSelected() )
                {
                  r2.setSelected(false);
                  r3.setSelected(false);
                  out.println("^+"+r1.getText());
                }
                else
                  out.println("^-"+r1.getText());
            }
        });
         r2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ( r2.isSelected() )
                {
                  r1.setSelected(false);
                  r3.setSelected(false);
                  out.println("^+"+r2.getText());
                }
                else
                  out.println("^-"+r2.getText());
            }
        });
        r3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ( r3.isSelected() )
                {
                  r1.setSelected(false);
                  r2.setSelected(false);
                  out.println("^+"+r3.getText());
                }
                else
                   out.println("^-"+r3.getText());
            }
        });
         
    
        
    }

    /**
     * Prompt for and return the address of the server.
     */
    private String getServerAddress() {
        return JOptionPane.showInputDialog(
            frame,
            "Enter IP Address of the Server:",
            "Welcome to PizzaChat",
            JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Prompt for and return the desired screen name.
     */
    private String getName() {
        return JOptionPane.showInputDialog(
            frame,
            "Choose a screen name:",
            "Screen name selection",
            JOptionPane.PLAIN_MESSAGE);
    }

     public String getDinnerType() {
    

        String[] options = {"Pizza", "Hero"};
        //  ImageIcon icon = new ImageIcon("src/images/turtle32.png");
        String n = (String)JOptionPane.showInputDialog(null, "Which Type of dinner",
                "Pizza", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
       // System.out.println(n);
        return(n);
    }    
    
    
    /**
     * Connects to the server then enters the processing loop.
     */
    private void run() throws IOException {
        dinnerType = getDinnerType();
        if ( dinnerType.contentEquals("Pizza"))
            dinnerIndex = 0;
        else if ( dinnerType.contentEquals("Hero"))
            dinnerIndex = 1;
        else
            dinnerIndex = 0;
        
        
        if ( dinnerType.contentEquals("Pizza") )
        {
  
           b1.setText(pizza.b1);
           b2.setText(pizza.b2);            
           b3.setText(pizza.b3);          
           b4.setText(pizza.b4);          
           b5.setText(pizza.b5);          
           b6.setText(pizza.b6);           
           b7.setText(pizza.b7);          
           b8.setText(pizza.b8);         
           b9.setText(pizza.b9);

            
           r1.setText(pizza.r1);
           r2.setText(pizza.r2);            
           r3.setText(pizza.r3);          
       
        } else if ( dinnerType.contentEquals("Hero") )
        {
  
           b1.setText(hero.b1);
           b2.setText(hero.b2);            
           b3.setText(hero.b3);          
           b4.setText(hero.b4);          
           b5.setText(hero.b5);          
           b6.setText(hero.b6);           
           b7.setText(hero.b7);          
           b8.setText(hero.b8);         
           b9.setText(hero.b9);

            
           r1.setText(hero.r1);
           r2.setText(hero.r2);            
           r3.setText(hero.r3);          
        }


        // System.out.println(dinnerType);
        // Make connection and initialize streams
        String serverAddress = getServerAddress();
        Socket socket = new Socket(serverAddress, 9001);
        
        
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Process all messages from server, according to the protocol.
        while (true) {
            String line = in.readLine();
            if (line.startsWith("SUBMITNAME")) {
                out.println(getName());
            } else if (line.startsWith("NAMEACCEPTED")) {
                textField.setEditable(true);
            } else if (line.startsWith("SYNC:")) {
                messageArea2.append (line.substring(6) + " just joined the Chat\n");   
                sendStatus();
            } else if (line.startsWith("SIZE: ")) {
                
                if ( debug.isSelected() )
                {
                    messageArea.append("<");
                    messageArea.append(line.substring(6));
                    messageArea.append(">\n");
                }
                
                /*
                if ( line.startsWith(r1.getText(),8) )
                    r0 = r1;
                else if ( line.startsWith(r2.getText(),8) )
                    r0 = r2;
                else if ( line.startsWith(r3.getText(),8) )
                    r0 = r3;
                */
                
                for ( JRadioButton r : radios ) {
                    if ( line.startsWith(r.getText(),8) ) {
                        r0 = r;
                        break;
                    }
                }

                //r1.setSelected(false);
                //r2.setSelected(false);
                //r3.setSelected(false);
                if (line.startsWith("+",7))
                   r0.setSelected(true);
                else
                   r0.setSelected(false);
                
                
            } else if (line.startsWith("STUFF: ")) {
                
                 if ( debug.isSelected() )
                    {               
                        messageArea.append("<");
                        messageArea.append(line.substring(7));
                        messageArea.append(">\n");
                    }
                 
            for ( JToggleButton b : buttons ) {
                String s = b.getText().substring(0,4);
                if ( line.startsWith(s ,8) )
                {
                    b0 = b;
                    break;
                }
            }
                  
               
    
                if (line.startsWith("+",7))
                    b0.setSelected(true);
                else
                    b0.setSelected(false);
                
                
            } else if (line.startsWith("MESSAGE")) {
                messageArea.append(line.substring(8) + "\n");
            }
        }
    }
  
   
 
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        DinnerOrder frame = new DinnerOrder("Chat");
        frame.setVisible(true);
        
        JavaChatClient client = new JavaChatClient();
        
        //  JFrame frame;
        client.frame = frame.getFrame();
        client.textField = frame.getTextField();
        client.messageArea = frame.getMessageArea();
        client.messageArea2 = frame.getMessageArea2();
        
        
        client.b1 = frame.b1();
        client.b2 = frame.b2();
        client.b3 = frame.b3();
        client.b4 = frame.b4();
        client.b5 = frame.b5();
        client.b6 = frame.b6();
        client.b7 = frame.b7();
        client.b8 = frame.b8();
        client.b9 = frame.b9();
        
        client.r1 = frame.r1();
        client.r2 = frame.r2();
        client.r3 = frame.r3();
        
        
         
        
       // array groupings
                 
        client.buttons.add(client.b1);
        client.buttons.add(client.b2);           
        client.buttons.add(client.b3);
        client.buttons.add(client.b4);
        client.buttons.add(client.b5);
        client.buttons.add(client.b6);
        client.buttons.add(client.b7);
        client.buttons.add(client.b8);
        client.buttons.add(client.b9);   
 
        client.radios.add(client.r1);
        client.radios.add(client.r2);           
        client.radios.add(client.r3);
    
        
        
        
        client.debug = frame.debug();
       
        client.place = frame.place();
        client.cancel = frame.cancel();
        client.refresh = frame.refresh();
         
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        
        client.setup();
        
        
        try {
             client.run();
        } catch ( Exception e)
        {
        }
        
        
    }
    
}

