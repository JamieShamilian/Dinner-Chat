/*
 */
package dinnerOrder_s;

/**
 *
 * @author Jamie Shamilian
 */

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


// json simple library from google 

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
 
import java.io.FileReader;
import java.util.Iterator;

public class JavaChatClient {

    BufferedReader in;
    PrintWriter out;

    JFrame frame;
    JTextField textField,textField1;
    JTextArea messageArea,messageArea2;
    JToggleButton debug;
    JButton place,cancel,refresh;
    String Order;
    
    // we put all the Buttons into an ArrayList
    // we update text of each button from json file
    ArrayList<JToggleButton> buttons = new ArrayList<>();

    // we put all the RadioButtons into an ArrayList
    // we update text of each radiobutton from json file    
    ArrayList<JRadioButton> radios = new ArrayList<>(); 

    // we have individual radiobuttons to insure only one is selected at a time.
    JRadioButton r1,r2,r3,r0;

    
    // dinnnerType points to text loaded from Menu.json
    String dinnerType=null;
         
    

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


     // 
     private void sendStatus()
        {
            
        if ( dinnerType != null ) {
            String sendtype = "$"+dinnerType;
            out.println(sendtype);
            
            /*
            messageArea.append("send[");
            messageArea.append(sendtype);
            messageArea.append("]\n");
            */
       //     System.out.println( sendtype );
        }
            
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

        // Add All the action Listeners

	// read chat window and send chat message to server
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

	// check radio buttons to verify order and then set order placed info and sendStatus to server via place order button
        place.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                Order = new String("No Size Selected ");
                
                for ( JRadioButton r : radios ) {
                    if ( r.isSelected() ) {
                        Order = ( r.getText() + ", ") ;
                        break;
                    }
                }
                
                if ( Order.startsWith("No") )
                {
                  messageArea2.append (Order+"Please select size\n");
                  return;
                }
                
                for ( JToggleButton b : buttons ) {
                    if ( b.isSelected() )
                        Order += ( b.getText() + ", ") ;
                }
                
                messageArea2.append (Order+"\n");      
                
                out.println("Placed Order:" + Order);
                
                for ( JRadioButton r : radios ) {
                    r.setSelected(false);
                 }
            
                for ( JToggleButton b : buttons ) {
                    b.setSelected(false);
                }
                
                sendStatus();

            }
        });

	// reset all the butons and sendStatus to server via cancel button
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
        

	// sendStatus to server via refresh button
        refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendStatus();
            }
        });
   
        
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
                  out.println("^-"+r2.getText());
                  r3.setSelected(false);
                  out.println("^-"+r3.getText());
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
                   out.println("^-"+r1.getText());
                  r3.setSelected(false);
                   out.println("^-"+r3.getText());
                  
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
                   out.println("^-"+r1.getText());
                  r2.setSelected(false);
                   out.println("^-"+r2.getText());
                   
                  out.println("^+"+r3.getText());
                }
                else
                   out.println("^-"+r3.getText());
            }
        });
         
    
        
    }

    /**
     * Prompt for and return the address of the server. Dialog
     */
    private String getServerAddress() {
        return JOptionPane.showInputDialog(
            frame,
            "Enter IP Address of the Server:",
            "Welcome to Dinner Chat",
            JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Prompt for and return the desired screen name. Dialog
     */
    private String getName() {
        return JOptionPane.showInputDialog(
            frame,
            "Choose a screen name:",
            "Screen name selection",
            JOptionPane.PLAIN_MESSAGE);
    }

     public String getDinnerType() {
    
        String[] options = null;
        int menucnt = 0;
        
        JSONParser parser = new JSONParser();
        Object obj;
 
               
	try {

	    // load the menu.json file
            obj = parser.parse(new FileReader("Menu.json"));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray menulist = (JSONArray) jsonObject.get("menulist");

            Iterator<JSONObject> iterator;

	    // count the number of menu items
            iterator = menulist.iterator();
            while (iterator.hasNext()) {
                menucnt++;
                //System.out.println(iterator.next());
                iterator.next();
            }
            
	    // setup options for dialog below
            options = new String[menucnt];
            for ( int i = 0; i < menucnt; i++ ) {
                String s = menulist.get(i).toString();;
                //System.out.println(s);
                options[i] = s;
            }

        } catch (Exception e) {
            e.printStackTrace();
	}

       
        String n = (String)JOptionPane.showInputDialog(null, "Which Type of dinner",
                "Pizza", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    
        return(n);
    }    
    
    void setButtonsFile() {
        
        JSONParser parser = new JSONParser();
        Object obj;
        
		try {
		    
		    // json file name is <dinnertype>.json
                    String fileType = dinnerType+".json";
		    
		    // create json parser from filename 
		    obj = parser.parse(new FileReader(fileType));
                    
		    // A JSON object. Key value pairs are unordered. JSONObject supports java.util.Map interface.
		    // load jsonObject 
		    JSONObject jsonObject = (JSONObject) obj;

		    // create the buttonlist from the string buttonlist in the json file
		    JSONArray buttonlist = (JSONArray) jsonObject.get("buttonlist");

		    // create the radiolist from the string radiolist in the json file
		    JSONArray radiolist = (JSONArray) jsonObject.get("radiolist");
		    
		    // initialize the name from the JSON file
		    String name = jsonObject.get("name").toString();
                    
		    // read the buttonslist and st the text of the buttons
		    for ( int i = 0; i < buttons.size(); i++)
                        {
                            JToggleButton b = buttons.get(i);
                            String s = buttonlist.get(i).toString();
                            b.setText(s);
                        }
		    
		    // read the radiolist and st the text of the radiobuttons
		    for ( int i = 0; i < radios.size(); i++)
                        {
                            JRadioButton r = radios.get(i);
                            String s = radiolist.get(i).toString();
                            r.setText(s);
                        }
		    
		    // set the name
		    textField1.setText(name);
		    
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
        
    }
     
     
     
    /**
     * Connects to the server then enters the processing loop.
     */
    private void run() throws IOException {
        
        
        // System.out.println(dinnerType);
        // Make connection and initialize streams

	// call getServerAddress above which shows dialog and returns a string of server address
        String serverAddress = getServerAddress();
	// returns a socket from serveraddress and fixed port number 9001
        Socket socket = new Socket(serverAddress, 9001);
        
	// setup input and output stream of socket to server
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        int lineCnt = 0;
        // Process all messages from server, according to the protocol.
        while (true) {
            
	    //messageArea.append("top input"+lineCnt+"\n");
            

	    // read from socket
	    String line = in.readLine();
            lineCnt++;

	    // help in debuging code
            /*
              messageArea.append("input<");
              messageArea.append(line);
              messageArea.append(">\n");
            */

	    // if server asks from my name
	    // I call getName and write my name to server
            if (line.startsWith("SUBMITNAME")) {
                out.println(getName());
            } else if (line.startsWith("NAMEACCEPTED")) {
		// if serve says name was accepted 
                textField.setEditable(true);

                // if server says name was accepted and
		// I am the first then call getDinnerType to choose from menu.json file
		// and setButtons from json File
                if ( line.startsWith("NAMEACCEPTED 1 ") )
            
                    if (dinnerType == null ){
                
			dinnerType = getDinnerType();
        
			setButtonsFile();
                    }
                
            } else if (line.startsWith("SYNC:")) {
		// if server ask me to send SYNC I call sendStatus
                messageArea2.append (line.substring(6) + " just joined the Chat\n");   
                sendStatus();
            } else if (line.startsWith("SIZE: ")) {
                // if server sends Size message set/ reset  radio buttons 
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
                
		// check which radio button was sent by server
                for ( JRadioButton r : radios ) {
                    if ( line.startsWith(r.getText(),8) ) {
                        r0 = r;
                        break;
                    }
                }

                //r1.setSelected(false);
                //r2.setSelected(false);
                //r3.setSelected(false);

		// set / reset radio if it was sent with a + 
		if (line.startsWith("+",7))
                   r0.setSelected(true);
                else
                   r0.setSelected(false);
                
                
            } else if (line.startsWith("STUFF: ")) {
                // if server sends STUFF message set / reset buttons 
                if ( debug.isSelected() )
                    {               
                        messageArea.append("<");
                        messageArea.append(line.substring(7));
                        messageArea.append(">\n");
                    }
		// make compile happy by always initializing b0
                JToggleButton b0 = buttons.get(0);
		// find which button is bing set/reset by comparing text
                for ( JToggleButton b : buttons ) {
                   // String s = b.getText().substring(0,4);
                    String s = b.getText();
                    if ( line.startsWith(s ,8) )
                    {
                        b0 = b;
                        break;
                    }
		}

		// set / reset button if sent with a +
                if (line.startsWith("+",7))
                    b0.setSelected(true);
                else
                    b0.setSelected(false);
                
                
            } else if (line.startsWith("MESSAGE")) {
		// if server sends MESSAGE message write it to messageArea
                messageArea.append(line.substring(8) + "\n");
            } else if (line.startsWith("TYPE")) {
		// if server sends TYPE message set dinnertype and read buttons from file
                dinnerType = line.substring(7);

		// debug 
                
		if ( debug.isSelected()) {
		    messageArea.append("<");
		    messageArea.append(line.substring(0));
		    messageArea.append(">\n");
		    
		    messageArea.append("<");
		    messageArea.append(line.substring(7));
		    messageArea.append(">\n");
		}
		
		// set buttons from file
                setButtonsFile();
            }

         //  messageArea.append("bottom input"+lineCnt+"\n");
                   
        }
    }
  
 
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // create the Java UI frame
        DinnerOrder frame = new DinnerOrder("Chat");
        frame.setVisible(true);
        

	// create the class JavaChatClient
	JavaChatClient client = new JavaChatClient();
        
        //  JFrame frame;
        client.frame = frame.getFrame();
        client.textField = frame.getTextField();
        client.textField1 = frame.getTextField1();
        client.messageArea = frame.getMessageArea();
        client.messageArea2 = frame.getMessageArea2();
        
	// create a copy  of the radiobuttons
        client.r1 = frame.r1();
        client.r2 = frame.r2();
        client.r3 = frame.r3();
        
	// create an arraylist of the buttons
        client.buttons.add(frame.b1());
        client.buttons.add(frame.b2());
        client.buttons.add(frame.b3());
        client.buttons.add(frame.b4());
        client.buttons.add(frame.b5());
        client.buttons.add(frame.b6());
        client.buttons.add(frame.b7());
        client.buttons.add(frame.b8());
        client.buttons.add(frame.b9());
        client.buttons.add(frame.b11());
        client.buttons.add(frame.b12());
        client.buttons.add(frame.b13());
        
	// create an arraylist of the radiobuttons
        client.radios.add(client.r1);
        client.radios.add(client.r2);           
        client.radios.add(client.r3);
    
	// copy of generic buttons
        client.debug = frame.debug();
       
        client.place = frame.place();
        client.cancel = frame.cancel();
        client.refresh = frame.refresh();
         
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        

	// call setup method above to setup action listener callbacks
        client.setup();
        
	// call the run loop above which has am infinte while loop 
	// inside of try to catch exceptions from run 
        try {
             client.run();
        } catch ( Exception e)
        {
        }
  
    }
    
}

