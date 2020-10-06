package GRA;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class KlientOknoPrzyciski extends JFrame  implements ActionListener
{
	private static final long serialVersionUID = -123888;
	private JButton bGreen;
	private JButton bBlue;
	private JButton bBlack;
	private JButton bWyslij;
	private JButton bUstawOdNowa;
	private JTextArea tWprowadz; // Pole do wprowadzania wiadomosci
	private static JTextArea tChat; // Pole do wyswietlania chatu i przebiegu gry
	private JLabel lTwojePoleGry;
	private JLabel lPoleGryPrzeciwnika;
	private JLabel lChat;
	private JLabel Jregulamin;
	private JLabel Jregulamin1;
	private JLabel Jregulamin2;
	private JLabel Jregulamin3;
	private JLabel Jregulamin4;
	private JLabel Jregulamin5;
	private JLabel Jregulamin6;
	
	private static JButton[][] PoleGracza = new JButton[10][10];
	private int[][] PoleGraczaInt = new int[12][12]; // 1- pole na ktorym jest statek 0-pole puste
	private static int[][] PolePrzeciwnikaInt = new int[12][12];
	private int x,y;
	private static String x2, y2; 
	private static int x3, y3;
	
	private static JButton[][] PolePrzeciwnika = new JButton[10][10];
	private int x1,y1;
	private int pierwsze_ustawienie=0;
	
	private static int rodzaj_informacji_int;
	private static String rodzaj_informacji_string;
	
	private int ilosc=0; // Zmienna która przechowuje ilosc ruchów gracza przy ustawieniu okrętu
	private int moje_punkty=0; // Zmienna która przechowuje ilosc punktów przydatna przy końcu gry
	private static int ruch_int=0; // Informuje o tym kto strzela domylnie gre zaczyna serwer
	private static String ruch_string;

	//Sieæ Klient Chat - Zmienne
    static Socket socket;
    static DataInputStream input;
    static DataOutputStream output;
    
    // Sieæ Klient Gra - Zmienne
    static ObjectInputStream wejscie;
    static ObjectOutputStream wyjscie;
    
	public KlientOknoPrzyciski()
	{
		setSize(1000,600);
		setTitle("Statki-Klient");
		setLayout(null);
		setLocation(955,200);
		// ETYKIETY 
		Jregulamin = new JLabel("REGULAMIN");
		Jregulamin.setFont(new Font("SansSerif",Font.BOLD,16));
		Jregulamin.setBounds(450,5,150,100);
		add(Jregulamin);
		
		Jregulamin1 = new JLabel("1. Okręty nie mogą stykać się masztami.");
		Jregulamin1.setFont(new Font("SansSerif",Font.BOLD,14));
		Jregulamin1.setBounds(350,40,350,100);
		add(Jregulamin1);
		
		Jregulamin2 = new JLabel("2. Gra się rozpoczyna gdy obaj  gracze  ");
		Jregulamin2.setFont(new Font("SansSerif",Font.BOLD,14));
		Jregulamin2.setBounds(350,60,350,100);
		add(Jregulamin2);
		
		Jregulamin3 = new JLabel("    ustawią swoje okręty. ");
		Jregulamin3.setFont(new Font("SansSerif",Font.BOLD,14));
		Jregulamin3.setBounds(350,80,350,100);
		add(Jregulamin3);
		
		Jregulamin4 = new JLabel("- okręt został trafiony.");
		Jregulamin4.setFont(new Font("SansSerif",Font.BOLD,14));
		Jregulamin4.setBounds(400,140,350,100);
		add(Jregulamin4);
		
		Jregulamin5 = new JLabel("- okręt nie został trafiony.");
		Jregulamin5.setFont(new Font("SansSerif",Font.BOLD,14));
		Jregulamin5.setBounds(400,180,350,100);
		add(Jregulamin5);
		
		Jregulamin6 = new JLabel("- okręt został zatopiony.");
		Jregulamin6.setFont(new Font("SansSerif",Font.BOLD,14));
		Jregulamin6.setBounds(400,220,350,100);
		add(Jregulamin6);
		
		lTwojePoleGry = new JLabel("Twoje pole gry");
		lTwojePoleGry.setFont(new Font("SansSerif",Font.BOLD,16));
		lTwojePoleGry.setBounds(40,5,150,100);
		add(lTwojePoleGry);
				
		lPoleGryPrzeciwnika = new JLabel("Pole gry przeciwnika");
		lPoleGryPrzeciwnika.setFont(new Font("SansSerif",Font.BOLD,16));
		lPoleGryPrzeciwnika.setBounds(650,5,180,100);
		add(lPoleGryPrzeciwnika);
				
		lChat = new JLabel("CHAT:");
		lChat.setFont(new Font("SansSerif",Font.BOLD,16));
		lChat.setBounds(10,360,180,50);
		add(lChat);
		
		bGreen = new JButton("");
		bGreen.setBounds(375,180,20,20);
		bGreen.setBackground(Color.GREEN);
		add(bGreen);
		
		bBlue = new JButton("");
		bBlue.setBounds(375,220,20,20);
		bBlue.setBackground(Color.BLUE);
		add(bBlue);
		
		bBlack = new JButton("");
		bBlack.setBounds(375,260,20,20);
		bBlack.setBackground(Color.BLACK);
		add(bBlack);
		
		
		bWyslij = new JButton("Wyslij");
		bWyslij.setBounds(10,525,100,20);
		add(bWyslij);
		bWyslij.addActionListener(this);
		
		bUstawOdNowa = new JButton("RESET");
		bUstawOdNowa.setBounds(150,525,100,20);
		add(bUstawOdNowa);
		bUstawOdNowa.addActionListener(this);
		
		tWprowadz = new JTextArea("");
		tWprowadz.setBounds(10,500,950,20);
		add(tWprowadz);
		tWprowadz.setToolTipText("Wpisz wiadomość do chatu");
		
		
		tChat = new JTextArea("");
		JScrollPane scrollChat = new JScrollPane(tChat);
		scrollChat.setBounds(10,400,950,90);
		add(scrollChat);
		tChat.setToolTipText("Chat");
		
		ustawPoleGryMoje();
		ustawPoleGryPrzeciwnika();
		
		//TŁO GRY
		
		JLabel background2;
		ImageIcon tlo = new ImageIcon("img/background2.jpg");
		
		background2 = new JLabel("",tlo,JLabel.CENTER);
		background2.setBounds(0,0,1000,600);
		add(background2);
		setVisible(true);

	}
	
	public static void utworzOkno()
	{
	KlientOknoPrzyciski aplikacja = new KlientOknoPrzyciski();
	aplikacja.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	aplikacja.setVisible(true);
	tChat.setText(tChat.getText().trim() + "\n" + "PODPOWIEDŹ: Ustaw teraz okręt 4-masztowy");
	
    try
    {
    	String msgin = "";
        socket=new Socket("localhost",1201);
        input=new DataInputStream(socket.getInputStream());
        output=new DataOutputStream(socket.getOutputStream());
        //wejscie = new ObjectInputStream(socket.getInputStream());
       // wyjscie = new ObjectOutputStream(socket.getOutputStream());
        
        while(!msgin.equals("exit"))
        {
        	rodzaj_informacji_string=input.readUTF();
        	rodzaj_informacji_int = Integer.parseInt(rodzaj_informacji_string);
        	if(rodzaj_informacji_int==100) { // Jeśli informacja jest z CHATU
            msgin=input.readUTF();
            tChat.setText(tChat.getText().trim() + "\n" + msgin);
        									}
        	if(rodzaj_informacji_int==200) { // Jeśli informacja jest z gry (wspóęrzedne pola przeciwnika)
				// Ustawienie okrętów na planszy przeciwnika
	            x2 =input.readUTF();
	        	y2 =input.readUTF();
	        	x3 =Integer.parseInt(x2);
	        	y3 =Integer.parseInt(y2);
	        	//PolePrzeciwnika[y3][x3].setBackground(Color.RED); // Aby było widać okręty przeciwnika
				PolePrzeciwnikaInt[x3+1][y3+1]=1; // Nawiązanie do tablicy int ktora ma informacje o zaznaczonych polach tablicy
        									}
        	if(rodzaj_informacji_int==300) { // TRAFIONY TWOJ OKRET
				// Ustawienie okrętów na planszy przeciwnika
	            x2 =input.readUTF();
	        	y2 =input.readUTF();
	        	x3 =Integer.parseInt(x2);
	        	y3 =Integer.parseInt(y2);
	        	PoleGracza[y3][x3].setBackground(Color.GREEN);
				 
        								   }
        	if(rodzaj_informacji_int==400) { // NIE TRAFIONY TWOJ OKRET
				// Ustawienie okrętów na planszy przeciwnika
	            x2 =input.readUTF();
	        	y2 =input.readUTF();
	        	x3 =Integer.parseInt(x2);
	        	y3 =Integer.parseInt(y2);
	        	PoleGracza[y3][x3].setBackground(Color.BLUE);
        									}
        	if(rodzaj_informacji_int==500) { // JELI PRZECIWNIK RESETUJE SWOJĄ PLANSZE
        		for(int i=0; i<PolePrzeciwnikaInt.length; i++)
    			{
  
    				for(int j=0; j<PolePrzeciwnikaInt[i].length;j++)
    				{
    					PolePrzeciwnikaInt[j][i]=0; // TO ZERUJ TABLICE
    					if(i<10&&j<10)
    					PolePrzeciwnika[i][j].setBackground(Color.GRAY);
    				}
    			}
        									}
        	if(rodzaj_informacji_int==700) { // INFORMACJA O RUCHU
        		ruch_string=input.readUTF();
        		ruch_int=Integer.parseInt(ruch_string);
        									}
		}   
    }
    catch(Exception exception)
        {
        exception.printStackTrace();
        }
	}
	
	public void ustawPoleGryMoje()
	   {
		x1=40;
		y1=40;
			for(int i=0; i<PoleGracza.length; i++)
			{
				y1=y1+30;
				x1=40;
				for(int j=0; j<PoleGracza[i].length;j++)
				{
					PoleGracza[i][j] = new JButton("");
					PoleGracza[i][j].setBounds(x1,y1,25,25);
					PoleGracza[i][j].setBackground(Color.GRAY);
					add(PoleGracza[i][j]);
					PoleGracza[i][j].addActionListener(this);
					x1=x1+30;
				}
			}
		}
	
	public void ustawPoleGryPrzeciwnika()
	   {
		x=650;
		y=40;
			for(int i=0; i<PolePrzeciwnika.length; i++)
			{
				y=y+30;
				x=650;
				for(int j=0; j<PolePrzeciwnika[i].length;j++)
				{
					PolePrzeciwnika[i][j] = new JButton("");
					PolePrzeciwnika[i][j].setBounds(x,y,25,25);
					PolePrzeciwnika[i][j].setBackground(Color.GRAY);
					add(PolePrzeciwnika[i][j]);
					PolePrzeciwnika[i][j].addActionListener(this);
					x=x+30;
				}
			}
		}
	
	
	//Akcje przyciskow
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object source = e.getSource();
		
		if(source == bWyslij)
		{
			String msgout="";
	        try{
	            msgout=tWprowadz.getText().trim();
	            output.writeUTF("100"); // 100 - wiadomosc z chatu - rodzaj info
	            output.writeUTF("Klient: "+msgout);
	            System.out.println("wysylam");
	            tChat.setText(tChat.getText().trim() + "\n" + "Klient: " + msgout);
	           }
	        catch(Exception exception)
	        {
	        	System.out.println(exception.getMessage());
	        }
		 }
		
		if(source == bUstawOdNowa)
		{
			if(ilosc<20)
			{
				tChat.setText(tChat.getText().trim() + "\n" + "INFO: ****************************************** ZRESETOWAŁEŚ SWOJĄ PLANSZĘ  ******************************************");
				try 
				{
					output.writeUTF("500"); // 500 - Gracz resetuje swoją plansze
				}
				catch(Exception exception)
				{
					System.out.println(exception.getMessage());
				}
				tChat.setText(tChat.getText().trim() + "\n" + "PODPOWIEDŹ: Ustaw teraz okręt 4-masztowy");
				ilosc=0;
				for(int i=0; i<PolePrzeciwnikaInt.length; i++)
				{
					for(int j=0; j<PolePrzeciwnikaInt[i].length;j++)
				{
					PoleGraczaInt[j][i]=0;
					if(i<10&&j<10)
					PoleGracza[i][j].setBackground(Color.GRAY);
				}
			
		    }
			
			}
			else
				tChat.setText(tChat.getText().trim() + "\n" + "INFO: *** ********************* NIEMOŻESZ JUŻ ZRESETOWAĆ SWOJEJ PLANSZY - GRA SIĘ ROZPOCZĘŁA ************************");
			
		}
		
		
		// PODPOWIEDZI
					if(ilosc==3)
						tChat.setText(tChat.getText().trim() + "\n" + "PODPOWIEDŹ: Ustaw teraz DWA okręty 3-masztowe");
					if(ilosc==9)
						tChat.setText(tChat.getText().trim() + "\n" + "PODPOWIEDŹ: Ustaw teraz TRZY okręty 2-masztowe");
					if(ilosc==15)
						tChat.setText(tChat.getText().trim() + "\n" + "PODPOWIEDŹ: Ustaw teraz CZTERY okręty 1-masztowe");
					if(ilosc==19){ // JeSli gracz ustwiL wszystkie okrety
						System.out.println("Ustawiłes juz wszystkie okręty"); // Komunikat w konsoli
						try{
							output.writeUTF("100"); // 100 - wiadomosc z chatu - rodzaj info
							output.writeUTF("INFO: Server ustawił wszystkie okręty"); // Wyslanie informacji do klienta
							System.out.println("wysylam");
							}
						catch(Exception exception)
						{
						System.out.println(exception.getMessage());
						}
						
						tChat.setText(tChat.getText().trim() + "\n" + "INFO: Ustawileś juz wszystkie okręty");
						
						
					  }
		
		
		try {
			// Ustawienie okretów na wlasnej planszy
			for(int i=0; i<PoleGracza.length; i++)
			{
				for(int j=0; j<PoleGracza[i].length;j++)
				{
					
					if(ilosc<20 && ilosc!=0)
					{
						
						// ************** 4 - MASZTOWIEC USTAWIENIE - SPRAWDZENIE B£ÊDÓW ******************
						
						if(source == PoleGracza[i][j] &&  
						(((  PoleGraczaInt[j][i+1]==1 || PoleGraczaInt[j+2][i+1]==1 || PoleGraczaInt[j+1][i]==1 ||  PoleGraczaInt[j+1][i+2]==1)&&ilosc==1 || 
						 (PoleGraczaInt[j][i+1]==1 && PoleGraczaInt[j-1][i+1]==1 && ilosc<4)||
						 (PoleGraczaInt[j+2][i+1]==1 && PoleGraczaInt[j+3][i+1]==1 && ilosc<4)||
						 (PoleGraczaInt[j+1][i]==1 && PoleGraczaInt[j+1][i-1]==1 && ilosc<4)||
						 (PoleGraczaInt[j+1][i+2]==1 && PoleGraczaInt[j+1][i+3]==1 && ilosc<4))||
						
						
						// ************** PIERWSZY 3 - MASZTOWIEC USTAWIENIE - SPRAWDZENIE B£ÊDÓW ******************
						(PoleGraczaInt[j+1][i+1]==0 && (PoleGraczaInt[j+1][i]==0   && PoleGraczaInt[j+1][i+2]==0   
						&& PoleGraczaInt[j][i+1]==0    && PoleGraczaInt[j+2][i+1]==0   && PoleGraczaInt[j][i]==0    && PoleGraczaInt[j+2][i+2]==0    
						&& PoleGraczaInt[j][i+2]==0   && PoleGraczaInt[j+2][i]==0   &&ilosc==4) || 
						(PoleGraczaInt[j][i+1]==1 || PoleGraczaInt[j+2][i+1]==1 || PoleGraczaInt[j+1][i]==1 ||  PoleGraczaInt[j+1][i+2]==1)&&ilosc==5||
						(PoleGraczaInt[j][i+1]==1 && PoleGraczaInt[j-1][i+1]==1 && ilosc<7 && ilosc>5)||
						(PoleGraczaInt[j+2][i+1]==1 && PoleGraczaInt[j+3][i+1]==1 && ilosc<7 && ilosc>5)||
						(PoleGraczaInt[j+1][i]==1 && PoleGraczaInt[j+1][i-1]==1 && ilosc<7 && ilosc>5)||
						(PoleGraczaInt[j+1][i+2]==1 && PoleGraczaInt[j+1][i+3]==1 && ilosc<7 && ilosc>5))||  
						// ************** DRUGI 3 - MASZTOWIEC USTAWIENIE - SPRAWDZENIE B£ÊDÓW ******************
						(PoleGraczaInt[j+1][i+1]==0 && (PoleGraczaInt[j+1][i]==0   && PoleGraczaInt[j+1][i+2]==0   
						&& PoleGraczaInt[j][i+1]==0    && PoleGraczaInt[j+2][i+1]==0   && PoleGraczaInt[j][i]==0    && PoleGraczaInt[j+2][i+2]==0    
						&& PoleGraczaInt[j][i+2]==0   && PoleGraczaInt[j+2][i]==0   &&ilosc==7) || 
						(PoleGraczaInt[j][i+1]==1 || PoleGraczaInt[j+2][i+1]==1 || PoleGraczaInt[j+1][i]==1 ||  PoleGraczaInt[j+1][i+2]==1)&&ilosc==8||
						(PoleGraczaInt[j][i+1]==1 && PoleGraczaInt[j-1][i+1]==1 && ilosc<10 && ilosc>8)||
						(PoleGraczaInt[j+2][i+1]==1 && PoleGraczaInt[j+3][i+1]==1 && ilosc<10 && ilosc>8)||
						(PoleGraczaInt[j+1][i]==1 && PoleGraczaInt[j+1][i-1]==1 && ilosc<10 && ilosc>8)||
						(PoleGraczaInt[j+1][i+2]==1 && PoleGraczaInt[j+1][i+3]==1 && ilosc<10 && ilosc>8))||
						// ************** PIERWSZY 2 - MASZTOWIEC USTAWIENIE - SPRAWDZENIE B£ÊDÓW ******************
						(PoleGraczaInt[j+1][i+1]==0 && (PoleGraczaInt[j+1][i]==0   && PoleGraczaInt[j+1][i+2]==0   
						&& PoleGraczaInt[j][i+1]==0    && PoleGraczaInt[j+2][i+1]==0   && PoleGraczaInt[j][i]==0    && PoleGraczaInt[j+2][i+2]==0    
						&& PoleGraczaInt[j][i+2]==0   && PoleGraczaInt[j+2][i]==0   &&ilosc==10)||
						(PoleGraczaInt[j][i+1]==1 || PoleGraczaInt[j+2][i+1]==1 || PoleGraczaInt[j+1][i]==1 ||  PoleGraczaInt[j+1][i+2]==1)&&ilosc==11)||
						// ************** DRUGI 2 - MASZTOWIEC USTAWIENIE - SPRAWDZENIE B£ÊDÓW ******************
						(PoleGraczaInt[j+1][i+1]==0 && (PoleGraczaInt[j+1][i]==0   && PoleGraczaInt[j+1][i+2]==0   
						&& PoleGraczaInt[j][i+1]==0    && PoleGraczaInt[j+2][i+1]==0   && PoleGraczaInt[j][i]==0    && PoleGraczaInt[j+2][i+2]==0    
						&& PoleGraczaInt[j][i+2]==0   && PoleGraczaInt[j+2][i]==0   &&ilosc==12)||
						(PoleGraczaInt[j][i+1]==1 || PoleGraczaInt[j+2][i+1]==1 || PoleGraczaInt[j+1][i]==1 ||  PoleGraczaInt[j+1][i+2]==1)&&ilosc==13)||																			
						// ************** TRZECI 2 - MASZTOWIEC USTAWIENIE - SPRAWDZENIE B£ÊDÓW ******************		
						(PoleGraczaInt[j+1][i+1]==0 && (PoleGraczaInt[j+1][i]==0   && PoleGraczaInt[j+1][i+2]==0   
						&& PoleGraczaInt[j][i+1]==0    && PoleGraczaInt[j+2][i+1]==0   && PoleGraczaInt[j][i]==0    && PoleGraczaInt[j+2][i+2]==0    
						&& PoleGraczaInt[j][i+2]==0   && PoleGraczaInt[j+2][i]==0   &&ilosc==14)||
						(PoleGraczaInt[j][i+1]==1 || PoleGraczaInt[j+2][i+1]==1 || PoleGraczaInt[j+1][i]==1 ||  PoleGraczaInt[j+1][i+2]==1)&&ilosc==15)||		
						// ************** PIERWSZY 1 - MASZTOWIEC USTAWIENIE - SPRAWDZENIE B£ÊDÓW ******************
						(PoleGraczaInt[j+1][i+1]==0 && (PoleGraczaInt[j+1][i]==0   && PoleGraczaInt[j+1][i+2]==0   
						&& PoleGraczaInt[j][i+1]==0    && PoleGraczaInt[j+2][i+1]==0   && PoleGraczaInt[j][i]==0    && PoleGraczaInt[j+2][i+2]==0    
						&& PoleGraczaInt[j][i+2]==0   && PoleGraczaInt[j+2][i]==0   &&ilosc==16))||
						// ************** DRUGI 1 - MASZTOWIEC USTAWIENIE - SPRAWDZENIE B£ÊDÓW ******************   
						(PoleGraczaInt[j+1][i+1]==0 && (PoleGraczaInt[j+1][i]==0   && PoleGraczaInt[j+1][i]==0   
						&& PoleGraczaInt[j][i+1]==0    && PoleGraczaInt[j+2][i+1]==0   && PoleGraczaInt[j][i]==0    && PoleGraczaInt[j+2][i+2]==0    
						&& PoleGraczaInt[j][i+2]==0   && PoleGraczaInt[j+2][i]==0   &&ilosc==17))||
						// ************** TRZECI 1 - MASZTOWIEC USTAWIENIE - SPRAWDZENIE B£ÊDÓW ****************** 
						(PoleGraczaInt[j+1][i+1]==0 && (PoleGraczaInt[j+1][i]==0   && PoleGraczaInt[j+1][i+2]==0   
						&& PoleGraczaInt[j][i+1]==0    && PoleGraczaInt[j+2][i+1]==0   && PoleGraczaInt[j][i]==0    && PoleGraczaInt[j+2][i+2]==0    
						&& PoleGraczaInt[j][i+2]==0   && PoleGraczaInt[j+2][i]==0   &&ilosc==18))||
						// ************** CZWARTY 1 - MASZTOWIEC USTAWIENIE - SPRAWDZENIE B£ÊDÓW ******************
						(PoleGraczaInt[j+1][i+1]==0 && (PoleGraczaInt[j+1][i]==0   && PoleGraczaInt[j+1][i+2]==0   
						&& PoleGraczaInt[j][i+1]==0    && PoleGraczaInt[j+2][i+1]==0   && PoleGraczaInt[j][i]==0    && PoleGraczaInt[j+2][i+2]==0    
						&& PoleGraczaInt[j][i+2]==0   && PoleGraczaInt[j+2][i]==0   &&ilosc==19))))
						{
							if(PoleGraczaInt[j][i]==1) // Zabezpieczenie przed ponownym kliknieciem tego samego buttona
							{
								ilosc--;
							}
							
							PoleGracza[i][j].setBackground(Color.RED);
							PoleGraczaInt[j+1][i+1]=1; // Nawiazanie do tablicy int ktora ma informacje o zaznaczonych polach tablicy
							ilosc++;
							
							try {
								output.writeUTF("200"); // 200 - Wiadomosc z gry rodzaj info
								output.writeUTF(""+(j)); // WSPÓL X
								output.writeUTF(""+(i)); // WSPÓL Y
								}
								catch(Exception exception)
								{
								System.out.println(exception.getMessage());
								}
							System.out.print("Kliknieto przcisk o współrzędnych: X = "+j );// Komunikat w konsoli
							System.out.println(" Y = "+i);// Komunikat w konsoli
							
							if(ilosc==20)
								try {
								output.writeUTF("600"); // 600 - wiadomosc o ustawieniu wszystkich okretow
								output.writeUTF(""+ilosc);
								    }
							    catch(Exception exception)
							    {
							    System.out.println(exception.getMessage());
							    }
						}
							
							
							
						}
					if(pierwsze_ustawienie==0 && ilosc==0 && source == PoleGracza[i][j]) // *** PIERWSZE USTAWIENIE ***
					{
						PoleGracza[i][j].setBackground(Color.RED);
						PoleGraczaInt[j+1][i+1]=1; // Nawiazanie do tablicy int ktora ma informacje o zaznaczonych polach tablicy
						ilosc++;
						pierwsze_ustawienie++;
						try {
							output.writeUTF("200"); // 200 - Wiadomosc z gry rodzaj info
							output.writeUTF(""+(j)); // WSPÓL X
							output.writeUTF(""+(i)); // WSPÓL Y
							}
							catch(Exception exception)
							{
							System.out.println(exception.getMessage());
							}
						System.out.print("Kliknieto przcisk o współrzędnych: X = "+j );// Komunikat w konsoli
						System.out.println(" Y = "+i);// Komunikat w konsoli
					}
					}
					
					
				}
			}
			catch(ArrayIndexOutOfBoundsException ex)
			{
			System.out.println(ex);
			
			}
		System.out.println(ilosc);
	
		if(ilosc==20 && ruch_int==1)  // Sprawdzenie czy gracz ustawil wszystkie okrêty aby móc rozpoczac grê
		{
			
	          // Sprawdzenie czy trafilismy okret 
					for(int i=0; i<PolePrzeciwnika.length; i++)
					{
						for(int j=0; j<PolePrzeciwnika[i].length;j++)
						{
							
							if(source == PolePrzeciwnika[i][j]) // Jeli zostal klikniety przycisk 
								{
								
								if(PolePrzeciwnikaInt[j+1][i+1]==1) //Jesli == 1 oznacza że trafilimy okret
								{
								    PolePrzeciwnikaInt[j+1][i+1]=2; // Oznaczenie okretu jako trafiony pomocne przy sprawdzeniu czy dany okret jest zatopiony
								 
								 // ZATOPIENIE - SPRAWDZANIE
								    
								    
								    if(PolePrzeciwnikaInt[j+1][i+1]==2 && PolePrzeciwnikaInt[j+2][i+1]==0 && PolePrzeciwnikaInt[j][i+1]==0 && PolePrzeciwnikaInt[j+1][i+2]==0 && PolePrzeciwnikaInt[j+1][i]==0)   // OKRÊT JEDNO MASZTOWY   
								        {
										     PolePrzeciwnika[i][j].setBackground(Color.BLACK); // Okret zostal zatopiony 
										}
								    else if(PolePrzeciwnikaInt[j+1][i+1]==2 && PolePrzeciwnikaInt[j][i+1]==2 && PolePrzeciwnikaInt[j-1][i+1]==0 && PolePrzeciwnikaInt[j+2][i+1]==0) // OKRET DWUMASZTOWY POZIOM w PRAWO
								        {
								    		PolePrzeciwnika[i][j].setBackground(Color.BLACK); // Okret zostal zatopiony
								    		PolePrzeciwnika[i][j-1].setBackground(Color.BLACK); // Okret zostal zatopiony
								        }
								    else if(PolePrzeciwnikaInt[j+1][i+1]==2 && PolePrzeciwnikaInt[j+2][i+1]==2 && PolePrzeciwnikaInt[j][i+1]==0 && PolePrzeciwnikaInt[j+3][i+1]==0) // OKRET DWUMASZTOWY POZIOM w LEWO
							        {
							    		PolePrzeciwnika[i][j].setBackground(Color.BLACK); // Okret zostal zatopiony
							    		PolePrzeciwnika[i][j+1].setBackground(Color.BLACK); // Okret zostal zatopiony
							        }
								    else if(PolePrzeciwnikaInt[j+1][i+1]==2 && PolePrzeciwnikaInt[j+1][i+2]==2 && PolePrzeciwnikaInt[j+1][i+3]==0 && PolePrzeciwnikaInt[j+1][i]==0) // OKRET DWUMASZTOWY PION w DÓ£
							        {
							    		PolePrzeciwnika[i][j].setBackground(Color.BLACK); // Okret zostal zatopiony
							    		PolePrzeciwnika[i+1][j].setBackground(Color.BLACK); // Okret zostal zatopiony
							        }
								    else if(PolePrzeciwnikaInt[j+1][i+1]==2 && PolePrzeciwnikaInt[j+1][i]==2 && PolePrzeciwnikaInt[j+1][i-1]==0 && PolePrzeciwnikaInt[j+1][i+2]==0) // OKRET DWUMASZTOWY PION w GÓRE
							        {
							    		PolePrzeciwnika[i][j].setBackground(Color.BLACK); // Okret zostal zatopiony
							    		PolePrzeciwnika[i-1][j].setBackground(Color.BLACK); // Okret zostal zatopiony
							        }
								    else if(PolePrzeciwnikaInt[j+1][i+1]==2 && PolePrzeciwnikaInt[j+2][i+1]==2 && PolePrzeciwnikaInt[j+3][i+1]==2 && PolePrzeciwnikaInt[j+4][i+1]==0 && PolePrzeciwnikaInt[j][i+1]==0) // OKRET TRZYMASZTOWY POZIOM w PRAWO
							        {
							    		PolePrzeciwnika[i][j].setBackground(Color.BLACK); // Okret zostal zatopiony
							    		PolePrzeciwnika[i][j+1].setBackground(Color.BLACK); // Okret zostal zatopiony
							    		PolePrzeciwnika[i][j+2].setBackground(Color.BLACK); // Okret zostal zatopiony
							        }
								    else if(PolePrzeciwnikaInt[j+1][i+1]==2 && PolePrzeciwnikaInt[j][i+1]==2 && PolePrzeciwnikaInt[j-1][i+1]==2 && PolePrzeciwnikaInt[j-3][i+1]==0 && PolePrzeciwnikaInt[j+2][i+1]==0) // OKRET TRZYMASZTOWY POZIOM w LEWO
							        {
							    		PolePrzeciwnika[i][j].setBackground(Color.BLACK); // Okret zostal zatopiony
							    		PolePrzeciwnika[i][j-1].setBackground(Color.BLACK); // Okret zostal zatopiony
							    		PolePrzeciwnika[i][j-2].setBackground(Color.BLACK); // Okret zostal zatopiony
							        }
								    else if(PolePrzeciwnikaInt[j+1][i+1]==2 && PolePrzeciwnikaInt[j+1][i+2]==2 && PolePrzeciwnikaInt[j+1][i+3]==2 && PolePrzeciwnikaInt[j+1][i+4]==0 && PolePrzeciwnikaInt[j+1][i]==0) // OKRET TRZYMASZTOWY PION w DÓ£
							        {
							    		PolePrzeciwnika[i][j].setBackground(Color.BLACK); // Okret zostal zatopiony
							    		PolePrzeciwnika[i+1][j].setBackground(Color.BLACK); // Okret zostal zatopiony
							    		PolePrzeciwnika[i+2][j].setBackground(Color.BLACK); // Okret zostal zatopiony
							        }
								    else if(PolePrzeciwnikaInt[j+1][i+1]==2 && PolePrzeciwnikaInt[j+1][i]==2 && PolePrzeciwnikaInt[j+1][i-1]==2 && PolePrzeciwnikaInt[j+1][i-2]==0 && PolePrzeciwnikaInt[j+1][i+2]==0) // OKRET TRZYMASZTOWY PION w GÓRÊ
							        {
							    		PolePrzeciwnika[i][j].setBackground(Color.BLACK); // Okret zostal zatopiony
							    		PolePrzeciwnika[i-1][j].setBackground(Color.BLACK); // Okret zostal zatopiony
							    		PolePrzeciwnika[i-2][j].setBackground(Color.BLACK); // Okret zostal zatopiony
							        }
								    else if(PolePrzeciwnikaInt[j+1][i+1]==2 && PolePrzeciwnikaInt[j+2][i+1]==2 && PolePrzeciwnikaInt[j+3][i+1]==2 && PolePrzeciwnikaInt[j+4][i+1]==2 && PolePrzeciwnikaInt[j+5][i+1]==0 && PolePrzeciwnikaInt[j][i+1]==0) // OKRET CZTEROMASZTOWY POZIOM w PRAWO
							        {
							    		PolePrzeciwnika[i][j].setBackground(Color.BLACK); // Okret zostal zatopiony
							    		PolePrzeciwnika[i][j+1].setBackground(Color.BLACK); // Okret zostal zatopiony
							    		PolePrzeciwnika[i][j+2].setBackground(Color.BLACK); // Okret zostal zatopiony
							    		PolePrzeciwnika[i][j+3].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
							        }
								    else if(PolePrzeciwnikaInt[j+1][i+1]==2 && PolePrzeciwnikaInt[j][i+1]==2 && PolePrzeciwnikaInt[j-1][i+1]==2 && PolePrzeciwnikaInt[j-2][i+1]==2 && PolePrzeciwnikaInt[j-3][i+1]==0 && PolePrzeciwnikaInt[j+2][i+1]==0) // OKRET CZTEROMASZTOWY POZIOM w LEWO
							        {
							    		PolePrzeciwnika[i][j].setBackground(Color.BLACK); // Okret zostal zatopiony
							    		PolePrzeciwnika[i][j-1].setBackground(Color.BLACK); // Okret zostal zatopiony
							    		PolePrzeciwnika[i][j-2].setBackground(Color.BLACK); // Okret zostal zatopiony
							    		PolePrzeciwnika[i][j-3].setBackground(Color.BLACK); // Okret zostal zatopiony
							        }
								    else if(PolePrzeciwnikaInt[j+1][i+1]==2 && PolePrzeciwnikaInt[j+1][i+2]==2 && PolePrzeciwnikaInt[j+1][i+3]==2 && PolePrzeciwnikaInt[j+1][i+4]==2 && PolePrzeciwnikaInt[j+1][i+5]==0 && PolePrzeciwnikaInt[j+1][i]==0) // OKRET CZTEROMASZTOWY PION w DÓ£
							        {
							    		PolePrzeciwnika[i][j].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
							    		PolePrzeciwnika[i+1][j].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
							    		PolePrzeciwnika[i+2][j].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
							    		PolePrzeciwnika[i+3][j].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
							        }
								    else if(PolePrzeciwnikaInt[j+1][i+1]==2 && PolePrzeciwnikaInt[j+1][i]==2 && PolePrzeciwnikaInt[j+1][i-1]==2 && PolePrzeciwnikaInt[j+1][i-2]==2 && PolePrzeciwnikaInt[j+1][i-3]==0 && PolePrzeciwnikaInt[j+1][i+2]==0) // OKRET CZTEROMASZTOWY PION w GÓRÊ
							        {
							    		PolePrzeciwnika[i][j].setBackground(Color.BLACK); // Okret zostal zatopiony
							    		PolePrzeciwnika[i-1][j].setBackground(Color.BLACK); // Okret zostal zatopiony
							    		PolePrzeciwnika[i-2][j].setBackground(Color.BLACK); // Okret zostal zatopiony
							    		PolePrzeciwnika[i-3][j].setBackground(Color.BLACK); // Okret zostal zatopiony
							        }
							     
								    else
								    	PolePrzeciwnika[i][j].setBackground(Color.GREEN); // Okret zostal trafiony
										
										try{
											output.writeUTF("100"); // 100 - wiadomosc z chatu - rodzaj info
											output.writeUTF("INFO: Klient trafił  twój okręt"); // Wysłanie informacji do serwera o trafieniu
											tChat.setText(tChat.getText().trim() + "\n" + "INFO: Trafiłeś okręt przeciwnika");
											output.writeUTF("300"); // 300 - Wiadomosc z gry rodzaj info - wspólrzedne z pola gry przeciwnika - 300 TRAFIONY
											output.writeUTF(""+j); // WSPÓL X
											output.writeUTF(""+i); // WSPÓL Y
											}
											catch(Exception exception)
											{
												System.out.println(exception.getMessage());
											}
										moje_punkty++;
										
									}
									if(PolePrzeciwnikaInt[j+1][i+1]==0) //Jesli == 0 oznacza ze nie trafilimy okrêtu
									{
										PolePrzeciwnika[i][j].setBackground(Color.BLUE); // Okret nie zostal trafiony
										try{
											output.writeUTF("100"); // 100 - wiadomosc z chatu - rodzaj info
											output.writeUTF("INFO: Klient nie trafił twojego okrętu"); // Wys³anie informacji do serwera o trafieniu
											tChat.setText(tChat.getText().trim() + "\n" + "INFO: Nie trafiłeś okrętu przeciwnika");
											output.writeUTF("400"); // 400 - Wiadomosc z gry rodzaj info - wspólrzedne z pola gry przeciwnika - 400 NIE TRAFIONY
											output.writeUTF(""+j); // WSPÓL X
											output.writeUTF(""+i); // WSPÓL Y
											ruch_int=0; // Jeli ruch=0 to kolejke ma serwer
											output.writeUTF("700"); // Do kolejnoci gry
											output.writeUTF(""+ruch_int);
											}
											catch(Exception exception)
											{
												System.out.println(exception.getMessage());
											}
									}
									if(moje_punkty==20) // Sprawdzenie koñca gry 
										{
											try{
											output.writeUTF("100"); // 100 - wiadomosc z chatu - rodzaj info
											output.writeUTF("INFO: Klient wygrywa trafił wszystkie okręty"); // Wyslanie informacji do klienta
											tChat.setText(tChat.getText().trim() + "\n" + "INFO: Wygrałeś grę trafiłeś wszystkie okręty");
											ruch_int=2; // KONIEC GRY ZABLOKOWANIE RUCHÓW
											output.writeUTF("700"); // Do kolejnosci gry
											output.writeUTF(""+ruch_int);
											}
											catch(Exception exception)
											{
												System.out.println(exception.getMessage());
											}
										}
									
								}
						
						}
					
					}
		}
									
										
										
										
										
	}
}								
							
						
					
					
					
					
	


