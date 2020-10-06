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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
 
 
public class SerwerOknoPrzyciski extends JFrame  implements ActionListener
{  
    private static final long serialVersionUID = -123882;
    private JButton bGreen;
    private JButton bBlue;
    private JButton bBlack;
    private JButton bWyslij;
    private JButton bUstawOdNowa;
    private JButton vsGracz, vsKomputer, wyjscie2;
    private JTextArea tWprowadz; // Pole do wprowadzania wiadomosci
    private JLabel lTwojePoleGry;
    private JLabel lPoleGryPrzeciwnika;
    private JLabel lChat;
    private static JTextArea tChat; // Pole do wyswietlania chatu i przebiegu gry
    private JLabel Jregulamin;
    private JLabel Jregulamin1;
    private JLabel Jregulamin2;
    private JLabel Jregulamin3;
    private JLabel Jregulamin4;
    private JLabel Jregulamin5;
    private JLabel Jregulamin6;
   
   
    private static JButton[][] PoleGracza = new JButton[10][10];
    private static int[][] PoleGraczaInt = new int[12][12]; // 1- pole na ktorym jest statek 0-pole puste 2-pole trafione
    private static int[][] PolePrzeciwnikaInt = new int[12][12];
    private int x,y;
    private static String x2, y2;
    private static int x3, y3;
   
    private static JButton[][] PolePrzeciwnika = new JButton[10][10];
    private int x1,y1;
    private int pierwsze_ustawienie=0;
   
    private static int rodzaj_informacji_int;
    private static String rodzaj_informacji_string;
   
    private static int rodzaj_okna = 0;
    private static int pierwsze_ustewienie_komputera = 0;
    private static int ilosc=0; // Zmienna która przechowuje ilosc ruchów gracza przy ustawieniu okrêtu
    private static String ilosc_przeciwnik_string;
    private static int ilosc_przeciwnik_int=0; // Zmienna uzywana przy sprawdzeniu czy przeciwnik ustawi³ swoje statki
    private int moje_punkty=0; // Zmienna która przechowuje ilosc punktów przydatna przy koñcu gry
    private static int ruch_int=0; // Informuje o tym kto strzela domylnie gre zaczyna serwer
    private static String ruch_string;
   
   
   
   
    //Sieæ Server Chat - zmienne
    static ServerSocket serversocket;
    static Socket socket;
    static DataInputStream input;
    static DataOutputStream output;
   
    //Sieæ Server Gra - zmienne
    static ObjectInputStream wejscie;
    static ObjectOutputStream wyjscie;
   
   
   
   
   
    public SerwerOknoPrzyciski()
    {
       
        if(rodzaj_okna==0)
        {
        JLabel background;
        setSize(400,600);
        setTitle("STATKI");
        setLayout(null);
        setLocation(800,260);
           
        ImageIcon gracz = new ImageIcon("img/vsGracz.png");
        vsGracz = new JButton(gracz);
        vsGracz.setBounds(120,100,150,60);
        add(vsGracz);
        vsGracz.addActionListener(this);
       
       
        ImageIcon komputer = new ImageIcon("img/vsKomputer.png");
        vsKomputer = new JButton(komputer);
        vsKomputer.setBounds(103,250,186,60);
        add(vsKomputer);
        vsKomputer.addActionListener(this);
       
       
        ImageIcon exit = new ImageIcon("img/wyjscie.png");
        wyjscie2= new JButton(exit);
       
        wyjscie2.setBounds(125,400,140,60);
        add(wyjscie2);
        wyjscie2.addActionListener(this);
       
        ImageIcon tlo = new ImageIcon("img/background.jpg");
       
        background = new JLabel("",tlo,JLabel.CENTER);
        background.setBounds(0,0,400,600);
        add(background);
        setVisible(true);
        }
       
       
        else if(rodzaj_okna==1)
        {
        setSize(1000,600);
        setTitle("Statki-Server");
        setLayout(null);
        setLocation(0,200);
       
       
       
       
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
       
       
       
       
    }
   
   
    public static boolean uruchomSerwer()
    {
        if(rodzaj_okna==0)
        {
        SerwerOknoPrzyciski aplikacja1 = new SerwerOknoPrzyciski();
        aplikacja1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        aplikacja1.setVisible(true);
        }
       
   
    try{
        String msgin="";
       
        if (rodzaj_okna==1)
        {
            SerwerOknoPrzyciski aplikacja = new SerwerOknoPrzyciski();
            aplikacja.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            aplikacja.setVisible(true);
            tChat.setText(tChat.getText().trim() + "\n" + "PODPOWIEDŹ: Ustaw teraz okręt 4-masztowy");
        }
       
        if(rodzaj_okna==0)
        {
       
        serversocket = new ServerSocket(1201); // Do chatu
        //serversocket = new ServerSocket(1202); // Do gry
        socket=serversocket.accept();
        input=new DataInputStream(socket.getInputStream()); // Do chatu
        output=new DataOutputStream(socket.getOutputStream()); // Do chatu
       
        }
        while(!msgin.equals("exit"))
        {
            rodzaj_informacji_string=input.readUTF();
            rodzaj_informacji_int = Integer.parseInt(rodzaj_informacji_string);
           
            if(rodzaj_informacji_int==100) { // Jeli informacja jest z CHATU
            msgin=input.readUTF();
            tChat.setText(tChat.getText().trim() + "\n" + msgin);
                                            }
            if(rodzaj_informacji_int==200) { // Jeli informacja jest z gry (wspó³rzêdne)
                // Ustawienie okrêtów na planszy przeciwnika
                x2 =input.readUTF();
                y2 =input.readUTF();
                x3 =Integer.parseInt(x2);
                y3 =Integer.parseInt(y2);
                //PolePrzeciwnika[y3][x3].setBackground(Color.RED); //Aby by³o widaæ okrêty przeciwnika
                PolePrzeciwnikaInt[x3+1][y3+1]=1; // Nawi¹zanie do tablicy int ktora ma informacje o zaznaczonych polach tablicy
                                            }
            if(rodzaj_informacji_int==300) { // TRAFIONY TWOJ OKRET
                // Ustawienie okrêtów na planszy przeciwnika
                x2 =input.readUTF();
                y2 =input.readUTF();
                x3 =Integer.parseInt(x2);
                y3 =Integer.parseInt(y2);
                PoleGracza[y3][x3].setBackground(Color.GREEN);
                 
                                           }
            if(rodzaj_informacji_int==400) { // NIE TRAFIONY TWOJ OKRET
                // Ustawienie okrêtów na planszy przeciwnika
                x2 =input.readUTF();
                y2 =input.readUTF();
                x3 =Integer.parseInt(x2);
                y3 =Integer.parseInt(y2);
                PoleGracza[y3][x3].setBackground(Color.BLUE);
                                            }
            if(rodzaj_informacji_int==500) { // JELI PRZECIWNIK RESETUJE SWOJ¥ PLANSZE
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
            if(rodzaj_informacji_int==600) { // INFORMACJA O USTAWIONYCH OKRETACH
                ilosc_przeciwnik_string =input.readUTF();
                ilosc_przeciwnik_int =Integer.parseInt(ilosc_przeciwnik_string);
               
                if(ilosc==20 && ilosc_przeciwnik_int==20) // Jeli gracze ustawili swoje okrêty wywietl odpowiednie komunikaty
                {
                tChat.setText(tChat.getText().trim() + "\n" + "INFO: SPRÓBUJ TRAFIĆ OKRĘT - ABY ROZPOCZĄĆ GRE");
                try{
                output.writeUTF("100"); // 100 - wiadomosc z chatu - rodzaj info
                output.writeUTF("INFO: GRE ROZPOCZYNA SERWER"); // Wys³anie informacji do klienta o trafieniu
                }
                catch(Exception exception)
                {
                    System.out.println(exception.getMessage());
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
   
   
        return true;
    }
   
   
   
   
    public static void komputer()
    {
        Random losuj = new Random();
       
        if(rodzaj_okna==0)
        {
        SerwerOknoPrzyciski aplikacja1 = new SerwerOknoPrzyciski();
        aplikacja1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        aplikacja1.setVisible(true);
        }
       
        if(rodzaj_okna==1)
        {
        SerwerOknoPrzyciski aplikacja = new SerwerOknoPrzyciski();
        aplikacja.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        aplikacja.setVisible(true);
        tChat.setText(tChat.getText().trim() + "\n" + "PODPOWIEDŹ: Ustaw teraz okręt 4-masztowy");
        rodzaj_okna++;
        }
       
       
        if(pierwsze_ustewienie_komputera==0)
        {
        int los = losuj.nextInt(3); // losujemy z zakresu 0 1 2 3
        if(los==0)
        {
          int[][] Pole0 = {
                  {0 , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                  {0 , 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0 },
                  {0 , 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0 },
                  {0 , 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0 },
                  {0 , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                  {0 , 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 },
                  {0 , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                  {0 , 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0 },
                  {0 , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                  {0 , 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
                  {0 , 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                  {0 , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
          };
         
          PolePrzeciwnikaInt = Pole0;
          ilosc_przeciwnik_int=20;
          pierwsze_ustewienie_komputera++;
        }
       
        if(los==1)
        {
          int[][] Pole1 = {
                  {0 , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                  {0 , 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0 },
                  {0 , 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0 },
                  {0 , 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0 },
                  {0 , 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0 },
                  {0 , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                  {0 , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                  {0 , 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
                  {0 , 0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 0 },
                  {0 , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                  {0 , 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0 },
                  {0 , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
          };
         
          PolePrzeciwnikaInt = Pole1;
          ilosc_przeciwnik_int=20;
          pierwsze_ustewienie_komputera++;
        }
       
        if(los==2)
        {
          int[][] Pole2 = {
                  {0 , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                  {0 , 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 },
                  {0 , 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0 },
                  {0 , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                  {0 , 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0 },
                  {0 , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                  {0 , 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
                  {0 , 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0 },
                  {0 , 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                  {0 , 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0 },
                  {0 , 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0 },
                  {0 , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
          };
         
          PolePrzeciwnikaInt = Pole2;
          ilosc_przeciwnik_int=20;
          pierwsze_ustewienie_komputera++;
        }
       
        if(los==3)
        {
          int[][] Pole3 = {
                  {0 , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                  {0 , 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
                  {0 , 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0 },
                  {0 , 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                  {0 , 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0 },
                  {0 , 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                  {0 , 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                  {0 , 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0 },
                  {0 , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                  {0 , 0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0 },
                  {0 , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                  {0 , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
          };
         
          PolePrzeciwnikaInt = Pole3;
          ilosc_przeciwnik_int=20;
          pierwsze_ustewienie_komputera++;
         }
        }
       
       
        sprawdz_koniec_gry(PoleGraczaInt);
       
        if(pierwsze_ustewienie_komputera==1 && ilosc == 20 && ruch_int==1) // jeśli komputer ma juz ustawione swoje okrety oraz gracz i jest ruch komputera
        {
                int x,y,wybor;
                x = losuj.nextInt(10);
                y = losuj.nextInt(10);
               
                System.out.println("x="+x);
                System.out.println("y="+y);
                if(PoleGraczaInt[y+1][x+1]==2 || PoleGraczaInt[y+1][x+1]==3) // zabezpieczenie przed ponownym wylosowaniem tych samych współrzędnych
                {
                    komputer();
                }
                if(PoleGraczaInt[y+1][x+1]==1) // Oznacza ze komputer trafil i ruch zostaje po jego stronie
                {
                    PoleGraczaInt[y+1][x+1]=2; // oznacz okret jako trafiony
                    PoleGracza[x][y].setBackground(Color.GREEN);
                    sprawdz_zatopienie_jednomasztowca(x,y);
                    sprawdz_zatopienie_trzymasztowca_poziomo(x, y);
                    sprawdz_zatopienie_trzymasztowca_pionowo(x, y);
                    sprawdz_zatopienie_czteromasztowca_pionowo(x, y);
                    sprawdz_zatopienie_czteromasztowca_poziomo(x, y);
                    sprawdz_koniec_gry(PoleGraczaInt);
                    algorytmy_pomocnicze(x,y);
                   
                       
                    if(x>0 && x<9 && y>0 && y<9) // Algorytm dla okrętów które nie zostały trafione przy krawędziach planszy
                    {
                        sprawdz_zatopienie_dwumasztowca_w_lewo(x, y);
                        sprawdz_zatopienie_dwumasztowca_w_prawo(x, y);
                        sprawdz_zatopienie_dwumasztowca_w_gore(x, y);
                        sprawdz_zatopienie_dwumasztowca_w_dol(x, y);
                       
                        wybor = losuj.nextInt(3);  // losuj sposrod 0 1 2 3
                        if(wybor==0)// sprawdzenie  w lewo
                            {
                                if(PoleGraczaInt[y][x+1]==1 || PoleGraczaInt[y][x+1]==2)
                                {
                                    PoleGraczaInt[y][x+1]=2;
                                    PoleGracza[x][y-1].setBackground(Color.GREEN);
                                    sprawdz_zatopienie_dwumasztowca_w_lewo(x, y);
                                    sprawdz_zatopienie_trzymasztowca_poziomo(x, y);
                                    sprawdz_zatopienie_trzymasztowca_pionowo(x, y);
                                    sprawdz_zatopienie_czteromasztowca_pionowo(x, y);
                                    sprawdz_zatopienie_czteromasztowca_poziomo(x, y);
                                    algorytmy_pomocnicze(x,y);
                                   
                                    ruch_int=1;
                                    sprawdz_koniec_gry(PoleGraczaInt);
                                    komputer();
                                }
                                else if(PoleGraczaInt[y][x+1]==0)
                                {
                                    PoleGracza[x][y-1].setBackground(Color.BLUE);
                                    PoleGraczaInt[y][x+1]=3;
                                    ruch_int=0;
                                }
                                else if(PoleGraczaInt[y][x+1]==2 || PoleGraczaInt[y][x+1]==3) // Jeśli wylosowane pole bylo juz "używane" zostal oddany strzal
                                {
                                    komputer();
                                }
                            }
                       
                        if(wybor==1) // sprawdzenie w prawo
                            {
                                if(PoleGraczaInt[y+2][x+1]==1 || PoleGraczaInt[y+2][x+1]==2)
                                {
                                    PoleGraczaInt[y+2][x+1]=2;
                                    PoleGracza[x][y+1].setBackground(Color.GREEN);
                                    sprawdz_zatopienie_dwumasztowca_w_prawo(x,y);
                                    sprawdz_zatopienie_trzymasztowca_poziomo(x, y);
                                    sprawdz_zatopienie_trzymasztowca_pionowo(x, y);
                                    sprawdz_zatopienie_czteromasztowca_pionowo(x, y);
                                    sprawdz_zatopienie_czteromasztowca_poziomo(x, y);
                                    algorytmy_pomocnicze(x,y);
                                    ruch_int=1;
                                    sprawdz_koniec_gry(PoleGraczaInt);
                                    komputer();
                                }
                                else if(PoleGraczaInt[y+2][x+1]==0)
                                {
                                    PoleGracza[x][y+1].setBackground(Color.BLUE);
                                    PoleGraczaInt[y+2][x+1]=3;
                                    ruch_int=0;
                                }
                                else if(PoleGraczaInt[y+2][x+1]==3) // Jeśli wylosowane pole bylo juz "używane" zostal oddany strzal
                                {
                                    komputer();
                                }
                            }
                       
                        if(wybor==2)
                            {
                                if(PoleGraczaInt[y+1][x+2]==1 || PoleGraczaInt[y+1][x+2]==2) // sprawdzenie  dol
                                {
                                    PoleGraczaInt[y+1][x+2]=2;
                                    PoleGracza[x+1][y].setBackground(Color.GREEN);
                                    sprawdz_zatopienie_dwumasztowca_w_dol(x,y);
                                    sprawdz_zatopienie_trzymasztowca_poziomo(x, y);
                                    sprawdz_zatopienie_trzymasztowca_pionowo(x, y);
                                    sprawdz_zatopienie_czteromasztowca_pionowo(x, y);
                                    sprawdz_zatopienie_czteromasztowca_poziomo(x, y);
                                    algorytmy_pomocnicze(x,y);
                                    ruch_int=1;
                                    sprawdz_koniec_gry(PoleGraczaInt);
                                    komputer();
                                }
                                else if(PoleGraczaInt[y+1][x+2]==0)
                                {
                                    PoleGracza[x+1][y].setBackground(Color.BLUE);
                                    PoleGraczaInt[y+1][x+2]=3;
                                    ruch_int=0;
                                }
                                else if(PoleGraczaInt[y+1][x+2]==3) // Jeśli wylosowane pole bylo juz "używane" zostal oddany strzal
                                {
                                    komputer();
                                }
                            }
                       
                        if(wybor==3)
                            {
                                if(PoleGraczaInt[y+1][x]==1 || PoleGraczaInt[y+1][x]==2) // // sprawdzenie  góra
                                {
                                    PoleGraczaInt[y+1][x]=2;
                                    PoleGracza[x-1][y].setBackground(Color.GREEN);
                                    sprawdz_zatopienie_dwumasztowca_w_gore(x,y);
                                    sprawdz_zatopienie_trzymasztowca_poziomo(x, y);
                                    sprawdz_zatopienie_trzymasztowca_pionowo(x, y);
                                    sprawdz_zatopienie_czteromasztowca_pionowo(x, y);
                                    sprawdz_zatopienie_czteromasztowca_poziomo(x, y);
                                    algorytmy_pomocnicze(x,y);
                                    ruch_int=1;
                                    sprawdz_koniec_gry(PoleGraczaInt);
                                    komputer();
                                }
                                else if(PoleGraczaInt[y+1][x]==0)
                                {
                                    PoleGracza[x-1][y].setBackground(Color.BLUE);
                                    PoleGraczaInt[y+1][x]=3;
                                    ruch_int=0;
                                }
                                else if(PoleGraczaInt[y+1][x]==2 || PoleGraczaInt[y+1][x]==3) // Jeśli wylosowane pole bylo juz "używane" zostal oddany strzal
                                {
                                    komputer();
                                }
                               
                            }
                       
                    }
                   
                    if(x==0 && y==0) // sprawdzenie lewy gorny rog
                    {
                    
                        sprawdz_zatopienie_dwumasztowca_w_prawo(x, y);
                        sprawdz_zatopienie_dwumasztowca_w_dol(x, y);
                        wybor = losuj.nextInt(1); // losuj sposrod 0 lub 1
                        if(wybor==0) // sprawdzenie  dol
                            {
                                if(PoleGraczaInt[y+1][x+2]==1 || PoleGraczaInt[y+1][x+2]==2)
                                {
                                    PoleGraczaInt[y+1][x+2]=2;
                                    PoleGracza[x+1][y].setBackground(Color.GREEN);
                                    sprawdz_zatopienie_dwumasztowca_w_dol(x,y);
                                    sprawdz_zatopienie_trzymasztowca_poziomo(x, y);
                                    sprawdz_zatopienie_trzymasztowca_pionowo(x, y);
                                    sprawdz_zatopienie_czteromasztowca_pionowo(x, y);
                                    sprawdz_zatopienie_czteromasztowca_poziomo(x, y);
                                    algorytmy_pomocnicze(x,y);
                                    ruch_int=1;
                                    sprawdz_koniec_gry(PoleGraczaInt);
                                    komputer();
                                }
                                else if(PoleGraczaInt[y+1][x+2]==0)
                                {
                                    PoleGracza[x+1][y].setBackground(Color.BLUE);
                                    PoleGraczaInt[y+1][x+2]=3;
                                    ruch_int=0;
                                }
                                else if(PoleGraczaInt[y+1][x+2]==2 || PoleGraczaInt[y+1][x+2]==3) // Jeśli wylosowane pole bylo juz "używane" zostal oddany strzal
                                {
                                    komputer();
                                }
                            }
                        if(wybor==1) // sprawdzenie  w prawa strone
                            {
                                if(PoleGraczaInt[y+2][x+1]==1 || PoleGraczaInt[y+2][x+1]==2)
                                {
                                    PoleGraczaInt[y+2][x+1]=2;
                                    PoleGracza[x][y+1].setBackground(Color.GREEN);
                                    sprawdz_zatopienie_dwumasztowca_w_prawo(x,y);
                                    sprawdz_zatopienie_trzymasztowca_poziomo(x, y);
                                    sprawdz_zatopienie_trzymasztowca_pionowo(x, y);
                                    sprawdz_zatopienie_czteromasztowca_pionowo(x, y);
                                    sprawdz_zatopienie_czteromasztowca_poziomo(x, y);
                                    algorytmy_pomocnicze(x,y);
                                    ruch_int=1;
                                    sprawdz_koniec_gry(PoleGraczaInt);
                                    komputer();
                                }
                                else if(PoleGraczaInt[y+2][x+1]==0)
                                {
                                    PoleGracza[x][y+1].setBackground(Color.BLUE);
                                    PoleGraczaInt[y+2][x+1]=3;
                                    ruch_int=0;
                                }
                                else if(PoleGraczaInt[y+2][x+1]==2 || PoleGraczaInt[y+2][x+1]==3) // Jeśli wylosowane pole bylo juz "używane" zostal oddany strzal
                                {
                                    komputer();
                                }
                            }
                       
                    }
                   
                    if(x==0 && y==9) // sprawdzenie prawy gorny rog
                    {
                        sprawdz_zatopienie_dwumasztowca_w_lewo(x, y);
                        sprawdz_zatopienie_dwumasztowca_w_dol(x, y);
                        wybor =  losuj.nextInt(1); // losuj sposrod 0 lub 1
                        if(wybor==0) // sprawdzenie  dol
                            {
                                if(PoleGraczaInt[y+1][x+2]==1 || PoleGraczaInt[y+1][x+2]==2)
                                {
                                    PoleGraczaInt[y+1][x+2]=2;
                                    PoleGracza[x+1][y].setBackground(Color.GREEN);
                                    sprawdz_zatopienie_dwumasztowca_w_dol(x,y);
                                    sprawdz_zatopienie_trzymasztowca_poziomo(x, y);
                                    sprawdz_zatopienie_trzymasztowca_pionowo(x, y);
                                    sprawdz_zatopienie_czteromasztowca_pionowo(x, y);
                                    sprawdz_zatopienie_czteromasztowca_poziomo(x, y);
                                    algorytmy_pomocnicze(x,y);
                                    ruch_int=1;
                                    sprawdz_koniec_gry(PoleGraczaInt);
                                    komputer();
                                   
                                }
                                else if(PoleGraczaInt[y+1][x+2]==0)
                                {
                                    PoleGracza[x+1][y].setBackground(Color.BLUE);
                                    PoleGraczaInt[y+1][x+2]=3;
                                    ruch_int=0;
                                }
                                else if( PoleGraczaInt[y+1][x+2]==3) // Jeśli wylosowane pole bylo juz "używane" zostal oddany strzal
                                {
                                    komputer();
                                }
                            }
                        if(wybor==1) // sprawdzenie w lewo
                            {
                                if(PoleGraczaInt[y][x+1]==1 || PoleGraczaInt[y][x+1]==2)
                                {
                                    PoleGraczaInt[y][x+1]=2;
                                    PoleGracza[x][y-1].setBackground(Color.GREEN);
                                    sprawdz_zatopienie_dwumasztowca_w_lewo(x, y);
                                    sprawdz_zatopienie_trzymasztowca_poziomo(x, y);
                                    sprawdz_zatopienie_trzymasztowca_pionowo(x, y);
                                    sprawdz_zatopienie_czteromasztowca_pionowo(x, y);
                                    sprawdz_zatopienie_czteromasztowca_poziomo(x, y);
                                    algorytmy_pomocnicze(x,y);
                                    ruch_int=1;
                                    sprawdz_koniec_gry(PoleGraczaInt);
                                    komputer();
                                }
                                else if(PoleGraczaInt[y][x+1]==0)
                                {
                                    PoleGracza[x][y-1].setBackground(Color.BLUE);
                                    PoleGraczaInt[y][x+1]=3;
                                    ruch_int=0;
                                }
                                else if( PoleGraczaInt[y][x+1]==3) // Jeśli wylosowane pole bylo juz "używane" zostal oddany strzal
                                {
                                    komputer();
                                }
                            }
                       
                    }
                   
                    if(x==9 && y==0) // sprawdzenie lewy dolny rog
                    {
                    
                        sprawdz_zatopienie_dwumasztowca_w_prawo(x, y);
                        sprawdz_zatopienie_dwumasztowca_w_gore(x, y);
                        wybor =  losuj.nextInt(1); // losuj sposrod 0 lub 1
                        if(wybor==0) // sprawdzenie  gora
                            {
                                if(PoleGraczaInt[y+1][x]==1 || PoleGraczaInt[y+1][x]==2)
                                {
                                    PoleGraczaInt[y+1][x]=2;
                                    PoleGracza[x-1][y].setBackground(Color.GREEN);
                                    sprawdz_zatopienie_dwumasztowca_w_gore(x,y);
                                    sprawdz_zatopienie_trzymasztowca_poziomo(x, y);
                                    sprawdz_zatopienie_trzymasztowca_pionowo(x, y);
                                    sprawdz_zatopienie_czteromasztowca_pionowo(x, y);
                                    sprawdz_zatopienie_czteromasztowca_poziomo(x, y);
                                    algorytmy_pomocnicze(x,y);
                                    ruch_int=1;
                                    sprawdz_koniec_gry(PoleGraczaInt);
                                    komputer();
                                }
                                else if(PoleGraczaInt[y+1][x]==0)
                                {
                                    PoleGracza[x-1][y].setBackground(Color.BLUE);
                                    PoleGraczaInt[y+1][x]=3;
                                    ruch_int=0;
                                }
                                else if( PoleGraczaInt[y+1][x]==3) // Jeśli wylosowane pole bylo juz "używane" zostal oddany strzal
                                {
                                    komputer();
                                }
                            }
                        if(wybor==1) // sprawdzenie  w prawo
                            {
                                if(PoleGraczaInt[y+2][x+1]==1 || PoleGraczaInt[y+2][x+1]==2)
                                {
                                    PoleGraczaInt[y+2][x+1]=2;
                                    PoleGracza[x][y+1].setBackground(Color.GREEN);
                                    sprawdz_zatopienie_dwumasztowca_w_prawo(x,y);
                                    sprawdz_zatopienie_trzymasztowca_poziomo(x, y);
                                    sprawdz_zatopienie_trzymasztowca_pionowo(x, y);
                                    sprawdz_zatopienie_czteromasztowca_pionowo(x, y);
                                    sprawdz_zatopienie_czteromasztowca_poziomo(x, y);
                                    algorytmy_pomocnicze(x,y);
                                    ruch_int=1;
                                    sprawdz_koniec_gry(PoleGraczaInt);
                                    komputer();
                                }
                                else if(PoleGraczaInt[y+2][x+1]==0)
                                {
                                    PoleGracza[x][y+1].setBackground(Color.BLUE);
                                    PoleGraczaInt[y+2][x+1]=3;
                                    ruch_int=0;
                                }
                                else if(PoleGraczaInt[y+2][x+1]==3) // Jeśli wylosowane pole bylo juz "używane" zostal oddany strzal
                                {
                                    komputer();
                                }
                            }
                       
                    }
                   
                    if(x==9 && y==9) // sprawdzenie prawy dolny rog
                    {
                        sprawdz_zatopienie_dwumasztowca_w_lewo(x, y);
                        sprawdz_zatopienie_dwumasztowca_w_gore(x, y);
                        wybor =  losuj.nextInt(1); // losuj sposrod 0 lub 1
                        if(wybor==0) // sprawdzenie  gora
                            {
                                if(PoleGraczaInt[y+1][x]==1 || PoleGraczaInt[y+1][x]==2)
                                {
                                    PoleGraczaInt[y+1][x]=2;
                                    PoleGracza[x-1][y].setBackground(Color.GREEN);
                                    sprawdz_zatopienie_dwumasztowca_w_gore(x,y);
                                    sprawdz_zatopienie_trzymasztowca_poziomo(x, y);
                                    sprawdz_zatopienie_trzymasztowca_pionowo(x, y);
                                    sprawdz_zatopienie_czteromasztowca_pionowo(x, y);
                                    sprawdz_zatopienie_czteromasztowca_poziomo(x, y);
                                    algorytmy_pomocnicze(x,y);
                                    ruch_int=1;
                                    sprawdz_koniec_gry(PoleGraczaInt);
                                    komputer();
                                }
                                else if(PoleGraczaInt[y+1][x]==0)
                                {
                                    PoleGracza[x-1][y].setBackground(Color.BLUE);
                                    PoleGraczaInt[y+1][x]=3;
                                    ruch_int=0;
                                }
                                else if (PoleGraczaInt[y+1][x]==3) // Jeśli wylosowane pole bylo juz "używane" zostal oddany strzal
                                {
                                    komputer();
                                }
                            }
                        if(wybor==1) // sprawdzenie lewa strona
                            {
                                if(PoleGraczaInt[y][x+1]==1 || PoleGraczaInt[y][x+1]==2)
                                {
                                    PoleGraczaInt[y][x+1]=2;
                                    PoleGracza[x][y-1].setBackground(Color.GREEN);
                                    sprawdz_zatopienie_dwumasztowca_w_lewo(x, y);
                                    sprawdz_zatopienie_trzymasztowca_poziomo(x, y);
                                    sprawdz_zatopienie_trzymasztowca_pionowo(x, y);
                                    sprawdz_zatopienie_czteromasztowca_pionowo(x, y);
                                    sprawdz_zatopienie_czteromasztowca_poziomo(x, y);
                                    algorytmy_pomocnicze(x,y);
                                    ruch_int=1;
                                    sprawdz_koniec_gry(PoleGraczaInt);
                                    komputer();
                                }
                                else if(PoleGraczaInt[y][x+1]==0)
                                {
                                    PoleGracza[x][y-1].setBackground(Color.BLUE);
                                    PoleGraczaInt[y][x+1]=3;
                                    ruch_int=0;
                                }
                                else if( PoleGraczaInt[y][x+1]==3) // Jeśli wylosowane pole bylo juz "używane" zostal oddany strzal
                                {
                                    komputer();
                                }
                            }
                       
                    }
                   
                   
                   
                   
                   
                   
                    if(y==0 && x>0 && x<9) // sprawdzenie lewa krawędz
                    {
                    	
                        sprawdz_zatopienie_dwumasztowca_w_prawo(x, y);
                        sprawdz_zatopienie_dwumasztowca_w_gore(x, y);
                        sprawdz_zatopienie_dwumasztowca_w_dol(x, y);
                        sprawdz_zatopienie_trzymasztowca_poziomo(x, y);
                        sprawdz_zatopienie_czteromasztowca_pionowo(x, y);
                        sprawdz_zatopienie_czteromasztowca_poziomo(x, y);
                        algorytmy_pomocnicze(x,y);
                        wybor = losuj.nextInt(2); // losuj sposrod 0 lub 1 lub 2 (góra,prawo,dól)
                        if(wybor==0) // sprawdzenie góra
                        {
                                if(PoleGraczaInt[y+1][x]==1 || PoleGraczaInt[y+1][x]==2)
                                {
                                    PoleGraczaInt[y+1][x]=2;
                                    PoleGracza[x-1][y].setBackground(Color.GREEN);
                                    sprawdz_zatopienie_dwumasztowca_w_gore(x,y);
                                    sprawdz_zatopienie_trzymasztowca_poziomo(x, y);
                                    sprawdz_zatopienie_trzymasztowca_pionowo(x, y);
                                    sprawdz_zatopienie_czteromasztowca_pionowo(x, y);
                                    sprawdz_zatopienie_czteromasztowca_poziomo(x, y);
                                    algorytmy_pomocnicze(x,y);
                                   
                                    ruch_int=1;
                                    sprawdz_koniec_gry(PoleGraczaInt);
                                    komputer();
                                }
                                else if(PoleGraczaInt[y+1][x]==0)
                                {
                                   
                                    PoleGracza[x-1][y].setBackground(Color.BLUE);
                                    PoleGraczaInt[y+1][x]=3;
                                    ruch_int=0;
                                }
                                else if(PoleGraczaInt[y+1][x]==3) // Jeśli wylosowane pole bylo juz "używane" zostal oddany strzal
                                {
                                    komputer();
                                }
                        }
                        if(wybor==1) // sprawdzenie prawo
                        {
                            if(PoleGraczaInt[y+2][x+1]==1 || PoleGraczaInt[y+2][x+1]==2)
                            {
                                PoleGraczaInt[y+2][x+1]=2;
                                PoleGracza[x][y+1].setBackground(Color.GREEN);
                                sprawdz_zatopienie_dwumasztowca_w_prawo(x,y);
                                sprawdz_zatopienie_trzymasztowca_poziomo(x, y);
                                sprawdz_zatopienie_trzymasztowca_pionowo(x, y);
                                sprawdz_zatopienie_czteromasztowca_pionowo(x, y);
                                sprawdz_zatopienie_czteromasztowca_poziomo(x, y);
                                algorytmy_pomocnicze(x,y);
                               
                                ruch_int=1;
                                sprawdz_koniec_gry(PoleGraczaInt);
                                komputer();
                               
                            }
                            else if(PoleGraczaInt[y+2][x+1]==0)
                            {
                                PoleGracza[x][y+1].setBackground(Color.BLUE);
                                PoleGraczaInt[y+2][x+1]=3;
                                ruch_int=0;
                            }
                            else if(PoleGraczaInt[y+2][x+1]==2 || PoleGraczaInt[y+2][x+1]==3) // Jeśli wylosowane pole bylo juz "używane" zostal oddany strzal
                            {
                                komputer();
                            }
                        }
                        if(wybor==2) // sprawdzenie dół
                        {
                            if(PoleGraczaInt[y+1][x+2]==1 || PoleGraczaInt[y+1][x+2]==2)
                            {
                                PoleGraczaInt[y+1][x+2]=2;
                                PoleGracza[x+1][y].setBackground(Color.GREEN);
                                sprawdz_zatopienie_dwumasztowca_w_dol(x,y);
                                sprawdz_zatopienie_trzymasztowca_pionowo(x, y);
                                sprawdz_zatopienie_czteromasztowca_pionowo(x, y);
                                sprawdz_zatopienie_czteromasztowca_poziomo(x, y);
                                algorytmy_pomocnicze(x,y);
                               
                                ruch_int=1;
                                sprawdz_koniec_gry(PoleGraczaInt);
                                komputer();
                            }
                            else if(PoleGraczaInt[y+1][x+2]==0)
                            {
                               
                                PoleGracza[x+1][y].setBackground(Color.BLUE);
                                PoleGraczaInt[y+1][x+2]=3;
                                ruch_int=0;
                            }
                            else if(PoleGraczaInt[y+1][x+2]==3) // Jeśli wylosowane pole bylo juz "używane" zostal oddany strzal
                            {
                                komputer();
                            }
                        }
                    }
                   
                   
                   
                   
                   
                   
                   
                   
                    if(y==9 && x>0 && x<9) // sprawdzenie prawa krawędz
                    {
                        sprawdz_zatopienie_dwumasztowca_w_lewo(x, y);
                        sprawdz_zatopienie_dwumasztowca_w_gore(x, y);
                        sprawdz_zatopienie_dwumasztowca_w_dol(x, y);
                        sprawdz_zatopienie_czteromasztowca_pionowo(x, y);
                        sprawdz_zatopienie_czteromasztowca_poziomo(x, y);
                        algorytmy_pomocnicze(x,y);
                        wybor = losuj.nextInt(2); // losuj sposrod 0 lub 1 lub 2 (góra,lewo,dól)
                        if(wybor==0) // sprawdzenie góra
                        {
                           
                                if(PoleGraczaInt[y+1][x]==1 || PoleGraczaInt[y+1][x]==2)
                                {
                                    PoleGraczaInt[y+1][x]=2;
                                    PoleGracza[x-1][y].setBackground(Color.GREEN);
                                    sprawdz_zatopienie_dwumasztowca_w_gore(x,y);
                                    sprawdz_zatopienie_trzymasztowca_pionowo(x, y);
                                    sprawdz_zatopienie_czteromasztowca_pionowo(x, y);
                                    sprawdz_zatopienie_czteromasztowca_poziomo(x, y);
                                    algorytmy_pomocnicze(x,y);
                                   
                                    ruch_int=1;
                                    sprawdz_koniec_gry(PoleGraczaInt);
                                    komputer();
                                   
                                }
                                else if(PoleGraczaInt[y+1][x]==0)
                                {
                                    PoleGracza[x-1][y].setBackground(Color.BLUE);
                                    PoleGraczaInt[y+1][x]=3;
                                    ruch_int=0;
                                }
                                else if( PoleGraczaInt[y+1][x]==3) // Jeśli wylosowane pole bylo juz "używane" zostal oddany strzal
                                {
                                    komputer();
                                }
                           
                        }
                        if(wybor==1) // sprawdzenie lewo
                        {
                            if(PoleGraczaInt[y][x+1]==1 || PoleGraczaInt[y][x+1]==2)
                            {
                                PoleGraczaInt[y][x+1]=2;
                                PoleGracza[x][y-1].setBackground(Color.GREEN);
                                sprawdz_zatopienie_dwumasztowca_w_lewo(x, y);
                                sprawdz_zatopienie_trzymasztowca_poziomo(x, y);
                                sprawdz_zatopienie_trzymasztowca_pionowo(x, y);
                                sprawdz_zatopienie_czteromasztowca_pionowo(x, y);
                                sprawdz_zatopienie_czteromasztowca_poziomo(x, y);
                                algorytmy_pomocnicze(x,y);
                               
                                ruch_int=1;
                                sprawdz_koniec_gry(PoleGraczaInt);
                                komputer();
                            }
                            else if(PoleGraczaInt[y][x+1]==0)
                            {
                                PoleGracza[x][y-1].setBackground(Color.BLUE);
                                PoleGraczaInt[y][x+1]=3;
                                ruch_int=0;
                            }
                            else if(PoleGraczaInt[y][x+1]==3) // Jeśli wylosowane pole bylo juz "używane" zostal oddany strzal
                            {
                                komputer();
                            }
                        }
                       
                        if(wybor==2) // sprawdzenie dół
                        {
                            if(PoleGraczaInt[y+1][x+2]==1 || PoleGraczaInt[y+1][x+2]==2)
                            {
                                PoleGraczaInt[y+1][x+2]=2;
                                PoleGracza[x+1][y].setBackground(Color.GREEN);
                                sprawdz_zatopienie_dwumasztowca_w_dol(x,y);
                                sprawdz_zatopienie_trzymasztowca_pionowo(x, y);
                                sprawdz_zatopienie_czteromasztowca_pionowo(x, y);
                                sprawdz_zatopienie_czteromasztowca_poziomo(x, y);
                                algorytmy_pomocnicze(x,y);
                               
                                ruch_int=1;
                                sprawdz_koniec_gry(PoleGraczaInt);
                                komputer();
                            }
                            else if(PoleGraczaInt[y+1][x+2]==0)
                            {
                                PoleGracza[x+1][y].setBackground(Color.BLUE);
                                PoleGraczaInt[y+1][x+2]=3;
                                ruch_int=0;
                            }
                            else if(PoleGraczaInt[y+1][x+2]==3) // Jeśli wylosowane pole bylo juz "używane" zostal oddany strzal
                            {
                                komputer();
                            }
                        }
                    }
                   
                   
                   
                   
                   
                    if(y>0 && y<9 && x==0) // sprawdzenie górna krawędz
                    {
                        sprawdz_zatopienie_dwumasztowca_w_lewo(x, y);
                        sprawdz_zatopienie_dwumasztowca_w_prawo(x, y);
                        sprawdz_zatopienie_dwumasztowca_w_dol(x, y);
                        sprawdz_zatopienie_czteromasztowca_pionowo(x, y);
                        sprawdz_zatopienie_czteromasztowca_poziomo(x, y);
                        algorytmy_pomocnicze(x,y);
                        wybor = losuj.nextInt(2); // losuj sposrod 0 lub 1 lub 2 (prawo,lewo,dól)
                        if(wybor==0) // sprawdzenie prawo
                        {
                            if(PoleGraczaInt[y+2][x+1]==1 || PoleGraczaInt[y+2][x+1]==2)
                            {
                                PoleGraczaInt[y+2][x+1]=2;
                                PoleGracza[x][y+1].setBackground(Color.GREEN);
                                sprawdz_zatopienie_dwumasztowca_w_prawo(x,y);
                                sprawdz_zatopienie_trzymasztowca_poziomo(x, y);
                                sprawdz_zatopienie_trzymasztowca_pionowo(x, y);
                                sprawdz_zatopienie_czteromasztowca_pionowo(x, y);
                                sprawdz_zatopienie_czteromasztowca_poziomo(x, y);
                                algorytmy_pomocnicze(x,y);
                               
                                ruch_int=1;
                                sprawdz_koniec_gry(PoleGraczaInt);
                                komputer();
                               
                            }
                            else if(PoleGraczaInt[y+2][x+1]==0)
                            {
                                PoleGracza[x][y+1].setBackground(Color.BLUE);
                                PoleGraczaInt[y+2][x+1]=3;
                                ruch_int=0;
                            }
                            else if(PoleGraczaInt[y+2][x+1]==3) // Jeśli wylosowane pole bylo juz "używane" zostal oddany strzal
                            {
                                komputer();
                            }
                        }
                        if(wybor==1) // sprawdzenie lewo
                        {
                            if(PoleGraczaInt[y][x+1]==1 || PoleGraczaInt[y][x+1]==2)
                            {
                                PoleGraczaInt[y][x+1]=2;
                                PoleGracza[x][y-1].setBackground(Color.GREEN);
                                sprawdz_zatopienie_dwumasztowca_w_lewo(x, y);
                                sprawdz_zatopienie_trzymasztowca_poziomo(x, y);
                                sprawdz_zatopienie_trzymasztowca_pionowo(x, y);
                                sprawdz_zatopienie_czteromasztowca_pionowo(x, y);
                                sprawdz_zatopienie_czteromasztowca_poziomo(x, y);
                                algorytmy_pomocnicze(x,y);
                               
                                ruch_int=1;
                                sprawdz_koniec_gry(PoleGraczaInt);
                                komputer();
                            }
                            else if(PoleGraczaInt[y][x+1]==0)
                            {
                                PoleGracza[x][y-1].setBackground(Color.BLUE);
                                PoleGraczaInt[y][x+1]=3;
                                ruch_int=0;
                            }
                            else if(PoleGraczaInt[y+2][x+1]==3) // Jeśli wylosowane pole bylo juz "używane" zostal oddany strzal
                            {
                                komputer();
                            }
                        }
                        if(wybor==2) // sprawdzenie dół
                        {
                            if(PoleGraczaInt[y+1][x+2]==1 || PoleGraczaInt[y+1][x+2]==2)
                            {
                                PoleGraczaInt[y+1][x+2]=2;
                                PoleGracza[x+1][y].setBackground(Color.GREEN);
                                sprawdz_zatopienie_dwumasztowca_w_dol(x,y);
                                sprawdz_zatopienie_trzymasztowca_pionowo(x, y);
                                sprawdz_zatopienie_czteromasztowca_pionowo(x, y);
                                sprawdz_zatopienie_czteromasztowca_poziomo(x, y);
                                algorytmy_pomocnicze(x,y);
                               
                                ruch_int=1;
                                sprawdz_koniec_gry(PoleGraczaInt);
                                komputer();
                            }
                            else if(PoleGraczaInt[y+1][x+2]==0)
                            {
                                PoleGracza[x+1][y].setBackground(Color.BLUE);
                                PoleGraczaInt[y+1][x+2]=3;
                                ruch_int=0;
                            }
                            else if(PoleGraczaInt[y+1][x+2]==2 || PoleGraczaInt[y+1][x+2]==3) // Jeśli wylosowane pole bylo juz "używane" zostal oddany strzal
                            {
                                komputer();
                            }
                        }
                    }
                   
                   
                   
                   
                   
                   
                   
                    if(y>0 && y<9 && x==9) // sprawdzenie dolna krawędz
                    {
                        sprawdz_zatopienie_dwumasztowca_w_lewo(x, y);
                        sprawdz_zatopienie_dwumasztowca_w_prawo(x, y);
                        sprawdz_zatopienie_dwumasztowca_w_gore(x, y);
                        wybor = losuj.nextInt(2); // losuj sposrod 0 lub 1 lub 2 (góra,prawo,lewo)
                        if(wybor==0) // sprawdzenie góra
                        {
                                if(PoleGraczaInt[y+1][x]==1 || PoleGraczaInt[y+1][x]==2)
                                {
                                    PoleGraczaInt[y+1][x]=2;
                                    PoleGracza[x-1][y].setBackground(Color.GREEN);
                                    sprawdz_zatopienie_dwumasztowca_w_gore(x,y);
                                    sprawdz_zatopienie_trzymasztowca_poziomo(x, y);
                                    sprawdz_zatopienie_trzymasztowca_pionowo(x, y);
                                    sprawdz_zatopienie_czteromasztowca_pionowo(x, y);
                                    sprawdz_zatopienie_czteromasztowca_poziomo(x, y);
                                    algorytmy_pomocnicze(x,y);
                                   
                                    ruch_int=1;
                                    sprawdz_koniec_gry(PoleGraczaInt);
                                    komputer();
                                }
                                else if(PoleGraczaInt[y+1][x]==0)
                                {
                                    PoleGracza[x-1][y].setBackground(Color.BLUE);
                                    PoleGraczaInt[y+1][x]=3;
                                    ruch_int=0;
                                }
                                else if(PoleGraczaInt[y+1][x]==3) // Jeśli wylosowane pole bylo juz "używane" zostal oddany strzal
                                {
                                    komputer();
                                }
                        }
                        if(wybor==1) // sprawdzenie prawo
                        {
                            if(PoleGraczaInt[y+2][x+1]==1 || PoleGraczaInt[y+2][x+1]==2)
                            {
                                PoleGraczaInt[y+2][x+1]=2;
                                PoleGracza[x][y+1].setBackground(Color.GREEN);
                                sprawdz_zatopienie_dwumasztowca_w_prawo(x,y);
                                sprawdz_zatopienie_trzymasztowca_poziomo(x, y);
                                sprawdz_zatopienie_trzymasztowca_pionowo(x, y);
                                sprawdz_zatopienie_czteromasztowca_pionowo(x, y);
                                sprawdz_zatopienie_czteromasztowca_poziomo(x, y);
                                algorytmy_pomocnicze(x,y);
                               
                                ruch_int=1;
                                sprawdz_koniec_gry(PoleGraczaInt);
                                komputer();
                               
                            }
                            else if(PoleGraczaInt[y+2][x+1]==0)
                            {
                                PoleGracza[x][y+1].setBackground(Color.BLUE);
                                PoleGraczaInt[y+2][x+1]=3;
                                ruch_int=0;
                            }
                            else if(PoleGraczaInt[y+2][x+1]==3) // Jeśli wylosowane pole bylo juz "używane" zostal oddany strzal
                            {
                                komputer();
                            }
                        }
                        if(wybor==2) // sprawdzenie lewo
                        {
                            if(PoleGraczaInt[y][x+1]==1 || PoleGraczaInt[y][x+1]==2)
                            {
                                PoleGraczaInt[y][x+1]=2;
                                PoleGracza[x][y-1].setBackground(Color.GREEN);
                                sprawdz_zatopienie_dwumasztowca_w_lewo(x, y);
                                sprawdz_zatopienie_trzymasztowca_poziomo(x, y);
                                sprawdz_zatopienie_trzymasztowca_pionowo(x, y);
                                sprawdz_zatopienie_czteromasztowca_pionowo(x, y);
                                sprawdz_zatopienie_czteromasztowca_poziomo(x, y);
                                algorytmy_pomocnicze(x,y);
                               
                                ruch_int=1;
                                sprawdz_koniec_gry(PoleGraczaInt);
                                komputer();
                            }
                            else if(PoleGraczaInt[y][x+1]==0)
                            {
                                PoleGracza[x][y-1].setBackground(Color.BLUE);
                                PoleGraczaInt[y][x+1]=3;
                                ruch_int=0;
                            }
                            else if(PoleGraczaInt[y][x+1]==3) // Jeśli wylosowane pole bylo juz "używane" zostal oddany strzal
                            {
                                komputer();
                            }
                        }
                    }
                }
               
               
               
               
               
               
               
               
               
               
               
               
               
               
               
               
               
               
               
               
               
               
               
               
               
               
                if (PoleGraczaInt[y+1][x+1]==0) // Oznacza ze komputer nie trafil i musi oddac ruch
                {
                    PoleGracza[x][y].setBackground(Color.BLUE);
                    PoleGraczaInt[y+1][x+1]=3;
                    ruch_int=0;
                }
           
        }
 
           
       
    }
   
   
    public static void sprawdz_zatopienie_jednomasztowca(int x, int y)
    {
        if((PoleGraczaInt[y][x]==0 || PoleGraczaInt[y][x]==3) && (PoleGraczaInt[y+1][x]==0 || PoleGraczaInt[y+1][x]==3) && (PoleGraczaInt[y+2][x]==0 || PoleGraczaInt[y+2][x]==3)
                  && (PoleGraczaInt[y+2][x+1]==0 || PoleGraczaInt[y+2][x+1]==3) && (PoleGraczaInt[y+2][x+2]==0 || PoleGraczaInt[y+2][x+2]==3) &&
                    (PoleGraczaInt[y+1][x+2]==0 || PoleGraczaInt[y+1][x+2]==3) && (PoleGraczaInt[y][x+2]==0 || PoleGraczaInt[y][x+2]==3)
                    && (PoleGraczaInt[y][x+1]==0 || PoleGraczaInt[y][x+1]==3) && PoleGraczaInt[y+1][x+1]==2 ) // Zatapianie jednomasztowca
                    {
                        PoleGraczaInt[y][x]=3;
                        PoleGraczaInt[y+1][x]=3;
                        PoleGraczaInt[y+2][x]=3;
                        PoleGraczaInt[y+2][x+1]=3;
                        PoleGraczaInt[y+2][x+2]=3;
                        PoleGraczaInt[y+1][x+2]=3;
                        PoleGraczaInt[y][x+2]=3;
                        PoleGraczaInt[y][x+1]=3;
                        PoleGracza[x][y].setBackground(Color.BLACK);
                        komputer();
                    }
    }
    
    public static void algorytm_pomocniczy1(int x, int y)
    {
    	 if(PoleGraczaInt[y+1][x+1]==2) 
                   {
                       PoleGraczaInt[y][x]=3;
                       PoleGraczaInt[y+2][x]=3;
                       PoleGraczaInt[y+2][x]=3;
                       PoleGraczaInt[y+2][x+2]=3; 
                   }
    }
    
   
    public static void sprawdz_zatopienie_dwumasztowca_w_gore(int x, int y)
    {
    	if(x>1)
    	{
        if((PoleGraczaInt[y][x-1]==0 || PoleGraczaInt[y][x-1]==3) && (PoleGraczaInt[y][x]==0 || PoleGraczaInt[y][x]==3) &&
                (PoleGraczaInt[y][x+1]==0 || PoleGraczaInt[y][x+1]==3) && (PoleGraczaInt[y][x+2]==0 || PoleGraczaInt[y][x+2]==3)  &&
                (PoleGraczaInt[y+1][x+2]==0 || PoleGraczaInt[y+1][x+2]==3) && (PoleGraczaInt[y+2][x+2]==0 || PoleGraczaInt[y+2][x+2]==3) &&
                (PoleGraczaInt[y+2][x+1]==0 || PoleGraczaInt[y+2][x+1]==3) && (PoleGraczaInt[y+2][x]==0 || PoleGraczaInt[y+2][x]==3) &&
                (PoleGraczaInt[y+2][x-1]==0 || PoleGraczaInt[y+2][x-1]==3) && (PoleGraczaInt[y+1][x-1]==0 || PoleGraczaInt[y+1][x-1]==3) &&
                 PoleGraczaInt[y+1][x+1]==2 && PoleGraczaInt[y+1][x]==2) // Zatapianie dwumasztowca w górę
                        {
                            PoleGraczaInt[y][x-1]=3;
                            PoleGraczaInt[y][x]=3;
                            PoleGraczaInt[y][x+1]=3;
                            PoleGraczaInt[y][x+2]=3;
                            PoleGraczaInt[y+1][x+2]=3;
                            PoleGraczaInt[y+2][x+2]=3;
                            PoleGraczaInt[y+2][x+1]=3;
                            PoleGraczaInt[y+2][x]=3;
                            PoleGraczaInt[y+2][x-1]=3;
                            PoleGraczaInt[y+1][x-1]=3;
                           
                            PoleGracza[x][y].setBackground(Color.BLACK);
                            PoleGracza[x-1][y].setBackground(Color.BLACK);
                        }
    	}
    }
    
    public static void algorytm_pomocniczy1_w_gore(int x, int y)
    {
    	if(x>1)
    	{
    	if(  PoleGraczaInt[y+1][x+1]==2 && PoleGraczaInt[y+1][x]==2) // Zatapianie dwumasztowca w górę
                        {
                            PoleGraczaInt[y][x-1]=3;
                            PoleGraczaInt[y][x]=3;
                            PoleGraczaInt[y][x+1]=3;
                            PoleGraczaInt[y][x+2]=3;
                            PoleGraczaInt[y+2][x+2]=3;
                            PoleGraczaInt[y+2][x+1]=3;
                            PoleGraczaInt[y+2][x]=3;
                            PoleGraczaInt[y+2][x-1]=3;
                      
                        }
    	}
    }
   
    public static void sprawdz_zatopienie_dwumasztowca_w_dol(int x, int y)
    {
    	if(x<9)
    	{
		    if((PoleGraczaInt[y][x]==0 || PoleGraczaInt[y][x]==3) && (PoleGraczaInt[y][x+1]==0 || PoleGraczaInt[y][x+1]==3) &&
		            (PoleGraczaInt[y][x+2]==0 || PoleGraczaInt[y][x+2]==3) && (PoleGraczaInt[y][x+3]==0 || PoleGraczaInt[y][x+3]==3)  &&
		            (PoleGraczaInt[y+1][x+3]==0 || PoleGraczaInt[y+1][x+3]==3) && (PoleGraczaInt[y+2][x+3]==0 || PoleGraczaInt[y+2][x+3]==3) &&
		            (PoleGraczaInt[y+2][x+2]==0 || PoleGraczaInt[y+2][x+2]==3) && (PoleGraczaInt[y+2][x+1]==0 || PoleGraczaInt[y+2][x+1]==3) &&
		            (PoleGraczaInt[y+2][x]==0 || PoleGraczaInt[y+2][x]==3) && (PoleGraczaInt[y+1][x]==0 || PoleGraczaInt[y+1][x]==3) &&
		             PoleGraczaInt[y+1][x+1]==2 && PoleGraczaInt[y+1][x+2]==2) // Zatapianie dwumasztowca w dół
		                    {
		                        PoleGraczaInt[y][x]=3;
		                        PoleGraczaInt[y][x+1]=3;
		                        PoleGraczaInt[y][x+2]=3;
		                        PoleGraczaInt[y][x+3]=3;
		                        PoleGraczaInt[y+1][x+3]=3;
		                        PoleGraczaInt[y+2][x+3]=3;
		                        PoleGraczaInt[y+2][x+2]=3;
		                        PoleGraczaInt[y+2][x+1]=3;
		                        PoleGraczaInt[y+2][x]=3;
		                        PoleGraczaInt[y+1][x]=3;
		                       
		                        PoleGracza[x][y].setBackground(Color.BLACK);
		                        PoleGracza[x+1][y].setBackground(Color.BLACK);
		                    }
    	}
    }
   
    public static void algorytm_pomocniczy1_w_dol(int x, int y)
    {
    	if(x<9)
    	{
    	if( PoleGraczaInt[y+1][x+1]==2 && PoleGraczaInt[y+1][x+2]==2) // Zatapianie dwumasztowca w dół
                        {
                            PoleGraczaInt[y][x]=3;
                            PoleGraczaInt[y][x+1]=3;
                            PoleGraczaInt[y][x+2]=3;
                            PoleGraczaInt[y][x+3]=3;
                            PoleGraczaInt[y+2][x+3]=3;
                            PoleGraczaInt[y+2][x+2]=3;
                            PoleGraczaInt[y+2][x+1]=3;
                            PoleGraczaInt[y+2][x]=3;

                        }
    	}
    }
    public static void sprawdz_zatopienie_dwumasztowca_w_prawo(int x, int y)
    {
    	if(y<9)
    	{
        if((PoleGraczaInt[y][x]==0 || PoleGraczaInt[y][x]==3) && (PoleGraczaInt[y][x+1]==0 || PoleGraczaInt[y][x+1]==3) &&
                (PoleGraczaInt[y][x+2]==0 || PoleGraczaInt[y][x+2]==3) && (PoleGraczaInt[y+1][x+2]==0 || PoleGraczaInt[y+1][x+2]==3)  &&
                (PoleGraczaInt[y+2][x+2]==0 || PoleGraczaInt[y+2][x+2]==3) && (PoleGraczaInt[y+3][x+2]==0 || PoleGraczaInt[y+3][x+2]==3) &&
                (PoleGraczaInt[y+3][x+1]==0 || PoleGraczaInt[y+3][x+1]==3) && (PoleGraczaInt[y+3][x]==0 || PoleGraczaInt[y+3][x]==3) &&
                (PoleGraczaInt[y+2][x]==0 || PoleGraczaInt[y+2][x]==3) && (PoleGraczaInt[y+1][x]==0 || PoleGraczaInt[y+1][x]==3) &&
                 PoleGraczaInt[y+1][x+1]==2 && PoleGraczaInt[y+2][x+1]==2) // Zatapianie dwumasztowca w lewo
                        {
                            PoleGraczaInt[y][x]=3;
                            PoleGraczaInt[y][x+1]=3;
                            PoleGraczaInt[y][x+2]=3;
                            PoleGraczaInt[y+1][x+2]=3;
                            PoleGraczaInt[y+2][x+2]=3;
                            PoleGraczaInt[y+3][x+2]=3;
                            PoleGraczaInt[y+3][x+1]=3;
                            PoleGraczaInt[y+3][x]=3;
                            PoleGraczaInt[y+2][x]=3;
                            PoleGraczaInt[y+1][x]=3;
                           
                            PoleGracza[x][y].setBackground(Color.BLACK);
                            PoleGracza[x][y+1].setBackground(Color.BLACK);
                        }
    	}
    }
    
    public static void algorytm_pomocniczy1_w_prawo(int x, int y)
    {
    	if(y<9)
    	{
    	if( PoleGraczaInt[y+1][x+1]==2 && PoleGraczaInt[y+2][x+1]==2) // Zatapianie dwumasztowca w lewo
                        {
                            PoleGraczaInt[y][x]=3;    
                            PoleGraczaInt[y][x+2]=3;
                            PoleGraczaInt[y+1][x+2]=3;
                            PoleGraczaInt[y+2][x+2]=3;
                            PoleGraczaInt[y+3][x+2]=3;   
                            PoleGraczaInt[y+3][x]=3;
                            PoleGraczaInt[y+2][x]=3;
                            PoleGraczaInt[y+1][x]=3;
                           
                       
                        }
    	}
    }
   
    public static void sprawdz_zatopienie_dwumasztowca_w_lewo(int x, int y)
    {
    	if(y>0)
    	{
        if((PoleGraczaInt[y-1][x]==0 || PoleGraczaInt[y-1][x]==3) && (PoleGraczaInt[y-1][x+1]==0 || PoleGraczaInt[y-1][x+1]==3) &&
                (PoleGraczaInt[y-1][x+2]==0 || PoleGraczaInt[y-1][x+2]==3) && (PoleGraczaInt[y][x+2]==0 || PoleGraczaInt[y][x+2]==3)  &&
                (PoleGraczaInt[y+2][x+2]==0 || PoleGraczaInt[y+2][x+2]==3) && (PoleGraczaInt[y+1][x+2]==0 || PoleGraczaInt[y+1][x+2]==3) &&
                (PoleGraczaInt[y+2][x+1]==0 || PoleGraczaInt[y+2][x+1]==3) && (PoleGraczaInt[y+2][x]==0 || PoleGraczaInt[y+2][x]==3) &&
                (PoleGraczaInt[y+1][x]==0 || PoleGraczaInt[y+1][x]==3) && (PoleGraczaInt[y][x]==0 || PoleGraczaInt[y][x]==3) &&
                 PoleGraczaInt[y+1][x+1]==2 && PoleGraczaInt[y][x+1]==2) // Zatapianie dwumasztowca w lewo
                        {
                            PoleGraczaInt[y-1][x]=3;
                            PoleGraczaInt[y-1][x+1]=3;
                            PoleGraczaInt[y-1][x+2]=3;
                            PoleGraczaInt[y][x+2]=3;
                            PoleGraczaInt[y+1][x+2]=3;
                            PoleGraczaInt[y+2][x+2]=3;
                            PoleGraczaInt[y+2][x+1]=3;
                            PoleGraczaInt[y+2][x]=3;
                            PoleGraczaInt[y+1][x]=3;
                            PoleGraczaInt[y][x]=3;
                           
                            PoleGracza[x][y].setBackground(Color.BLACK);
                            PoleGracza[x][y-1].setBackground(Color.BLACK);
                        }
    	}
    }
    
    public static void algorytm_pomocniczy1_w_lewo(int x, int y)
    {
    	if(y>0)
    	{
    	if(PoleGraczaInt[y+1][x+1]==2 && PoleGraczaInt[y][x+1]==2) // Zatapianie dwumasztowca w lewo
                        {
                            PoleGraczaInt[y-1][x]=3;
                            PoleGraczaInt[y-1][x+2]=3;
                            PoleGraczaInt[y][x+2]=3;
                            PoleGraczaInt[y+1][x+2]=3;
                            PoleGraczaInt[y+2][x+2]=3;
                            PoleGraczaInt[y+2][x]=3;
                            PoleGraczaInt[y+1][x]=3;
                            PoleGraczaInt[y][x]=3;
                        }
    	}
    }
   
    
    public static void  sprawdz_zatopienie_trzymasztowca_trafiony_w_srodku_pionowo(int x,int y)
    {
        if(x>0 && x<9)
        {
        if ((PoleGraczaInt[y+1][x-1]==0 || PoleGraczaInt[y+1][x-1]==3) && (PoleGraczaInt[y+1][x+3]==0 || PoleGraczaInt[y+1][x+3]==3) &&
                 PoleGraczaInt[y+1][x+1]==2 && PoleGraczaInt[y+1][x]==2 && PoleGraczaInt[y+1][x+2]==2)
                        {
                            PoleGraczaInt[y][x-1]=3;
                            PoleGraczaInt[y][x]=3;
                            PoleGraczaInt[y][x+1]=3;
                            PoleGraczaInt[y][x+2]=3;
                            PoleGraczaInt[y][x+3]=3;
                            PoleGraczaInt[y+1][x+3]=3;
                            PoleGraczaInt[y+2][x+3]=3;
                            PoleGraczaInt[y+2][x+2]=3;
                            PoleGraczaInt[y+2][x+1]=3;
                            PoleGraczaInt[y+2][x]=3;
                            PoleGraczaInt[y+2][x-1]=3;
                            PoleGraczaInt[y+1][x-1]=3;
                           
                            PoleGracza[x][y].setBackground(Color.BLACK);
                            PoleGracza[x-1][y].setBackground(Color.BLACK);
                            PoleGracza[x+1][y].setBackground(Color.BLACK);
                        }
        }
    }
    
    public static void algorytm_pomocniczy2_w_srodku_pionowo(int x, int y)
    {
    	if(x>0 && x<9)
        {
        if (PoleGraczaInt[y+1][x+1]==2 && PoleGraczaInt[y+1][x]==2 && PoleGraczaInt[y+1][x+2]==2)
                        {
                            PoleGraczaInt[y][x-1]=3;
                            PoleGraczaInt[y][x]=3;
                            PoleGraczaInt[y][x+1]=3;
                            PoleGraczaInt[y][x+2]=3;
                            PoleGraczaInt[y][x+3]=3;
                            PoleGraczaInt[y+2][x+3]=3;
                            PoleGraczaInt[y+2][x+2]=3;
                            PoleGraczaInt[y+2][x+1]=3;
                            PoleGraczaInt[y+2][x]=3;
                            PoleGraczaInt[y+2][x-1]=3; 
                        }
        }
    }
   
    public static void sprawdz_zatopienie_trzymasztowca_trafiony_w_srodku_poziomo(int x, int y)
    {
        if(y>0 && y<9)
        {
        if ((PoleGraczaInt[y-1][x+1]==0 || PoleGraczaInt[y-1][x+1]==3) && (PoleGraczaInt[y+3][x+1]==0 || PoleGraczaInt[y+3][x+1]==3) &&
         PoleGraczaInt[y+1][x+1]==2 && PoleGraczaInt[y][x+1]==2 && PoleGraczaInt[y+2][x+1]==2)
                {
                    PoleGraczaInt[y-1][x+1]=3;
                    PoleGraczaInt[y+3][x+1]=3;
                    PoleGraczaInt[y-1][x]=3;
                    PoleGraczaInt[y][x]=3;
                    PoleGraczaInt[y+1][x]=3;
                    PoleGraczaInt[y+2][x]=3;
                    PoleGraczaInt[y+3][x]=3;
                    PoleGraczaInt[y+3][x+2]=3;
                    PoleGraczaInt[y+2][x+2]=3;
                    PoleGraczaInt[y+1][x+2]=3;
                    PoleGraczaInt[y][x+2]=3;
                    PoleGraczaInt[y-1][x+2]=3;
                   
                    PoleGracza[x][y].setBackground(Color.BLACK);
                    PoleGracza[x][y-1].setBackground(Color.BLACK);
                    PoleGracza[x][y+1].setBackground(Color.BLACK);
                }
        }
    }
    
    public static void algorytm_pomocniczy2_w_srodku_poziomo(int x, int y)
    {
    	if(y>0 && y<9)
        {
        if (PoleGraczaInt[y+1][x+1]==2 && PoleGraczaInt[y][x+1]==2 && PoleGraczaInt[y+2][x+1]==2)
                {
                    PoleGraczaInt[y-1][x]=3;
                    PoleGraczaInt[y][x]=3;
                    PoleGraczaInt[y+1][x]=3;
                    PoleGraczaInt[y+2][x]=3;
                    PoleGraczaInt[y+3][x]=3;
                    PoleGraczaInt[y+3][x+2]=3;
                    PoleGraczaInt[y+2][x+2]=3;
                    PoleGraczaInt[y+1][x+2]=3;
                    PoleGraczaInt[y][x+2]=3;
                    PoleGraczaInt[y-1][x+2]=3;
                
                }
        }
    }
   
    public static void sprawdz_zatopienie_trzymasztowca_trafiony_z_gory(int x, int y)
    {
        if(x<8)
        {
        if ((PoleGraczaInt[y+1][x]==0 || PoleGraczaInt[y+1][x]==3) && (PoleGraczaInt[y+1][x+4]==0 || PoleGraczaInt[y+1][x+4]==3) &&
                PoleGraczaInt[y+1][x+1]==2 && PoleGraczaInt[y+1][x+2]==2 && PoleGraczaInt[y+1][x+3]==2)
                        {
                            PoleGraczaInt[y][x]=3;
                            PoleGraczaInt[y][x+1]=3;
                            PoleGraczaInt[y][x+2]=3;
                            PoleGraczaInt[y][x+3]=3;
                            PoleGraczaInt[y][x+4]=3;
                            PoleGraczaInt[y+1][x+4]=3;
                            PoleGraczaInt[y+2][x+4]=3;
                            PoleGraczaInt[y+2][x+3]=3;
                            PoleGraczaInt[y+2][x+2]=3;
                            PoleGraczaInt[y+2][x+1]=3;
                            PoleGraczaInt[y+2][x]=3;
                            PoleGraczaInt[y+1][x]=3;
                           
                            PoleGracza[x][y].setBackground(Color.BLACK);
                            PoleGracza[x+1][y].setBackground(Color.BLACK);
                            PoleGracza[x+2][y].setBackground(Color.BLACK);
                        }
        }
    }
    
    public static void algorytm_pomocniczy2_w_gore(int x, int y)
    {
    	 if(x<8)
         {
         if ( PoleGraczaInt[y+1][x+1]==2 && PoleGraczaInt[y+1][x+2]==2 && PoleGraczaInt[y+1][x+3]==2)
                         {
                             PoleGraczaInt[y][x]=3;
                             PoleGraczaInt[y][x+1]=3;
                             PoleGraczaInt[y][x+2]=3;
                             PoleGraczaInt[y][x+3]=3;
                             PoleGraczaInt[y][x+4]=3;
                             PoleGraczaInt[y+2][x+4]=3;
                             PoleGraczaInt[y+2][x+3]=3;
                             PoleGraczaInt[y+2][x+2]=3;
                             PoleGraczaInt[y+2][x+1]=3;
                             PoleGraczaInt[y+2][x]=3;           
                         }
         }
    }
    public static void sprawdz_zatopienie_trzymasztowca_trafiony_z_lewej(int x, int y)
    {
        if(y<8)
        {
        if ((PoleGraczaInt[y][x+1]==0 || PoleGraczaInt[y][x+1]==3) && (PoleGraczaInt[y+4][x+1]==0 || PoleGraczaInt[y+4][x+1]==3) &&
                 PoleGraczaInt[y+1][x+1]==2 && PoleGraczaInt[y+2][x+1]==2 && PoleGraczaInt[y+3][x+1]==2) // Zatapianie dwumasztowca w lewo
                        {
                            PoleGraczaInt[y][x]=3;
                            PoleGraczaInt[y][x+1]=3;
                            PoleGraczaInt[y][x+2]=3;
                            PoleGraczaInt[y+1][x+2]=3;
                            PoleGraczaInt[y+2][x+2]=3;
                            PoleGraczaInt[y+3][x+2]=3;
                            PoleGraczaInt[y+4][x+2]=3;
                            PoleGraczaInt[y+4][x+1]=3;
                            PoleGraczaInt[y+4][x]=3;
                            PoleGraczaInt[y+3][x]=3;
                            PoleGraczaInt[y+2][x]=3;
                            PoleGraczaInt[y+1][x]=3;
                           
                            PoleGracza[x][y].setBackground(Color.BLACK);
                            PoleGracza[x][y+1].setBackground(Color.BLACK);
                            PoleGracza[x][y+2].setBackground(Color.BLACK);
                        }
        }
    }
    
    public static void algorytm_pomocniczy2_w_lewo(int x, int y)
    {
    	 if(y<8)
         {
         if (PoleGraczaInt[y+1][x+1]==2 && PoleGraczaInt[y+2][x+1]==2 && PoleGraczaInt[y+3][x+1]==2) // Zatapianie dwumasztowca w lewo
                         {
                             PoleGraczaInt[y][x]=3;
                             PoleGraczaInt[y][x+2]=3;
                             PoleGraczaInt[y+1][x+2]=3;
                             PoleGraczaInt[y+2][x+2]=3;
                             PoleGraczaInt[y+3][x+2]=3;
                             PoleGraczaInt[y+4][x+2]=3;
                             PoleGraczaInt[y+4][x]=3;
                             PoleGraczaInt[y+3][x]=3;
                             PoleGraczaInt[y+2][x]=3;
                             PoleGraczaInt[y+1][x]=3;
                          
                         }
         }
    }
   
    public static void sprawdz_zatopienie_trzymasztowca_trafiony_z_dolu(int x, int y)
    {
        if(x>1)
        {
            if ((PoleGraczaInt[y+1][x-2]==0 || PoleGraczaInt[y+1][x-2]==3) && (PoleGraczaInt[y+1][x+2]==0 || PoleGraczaInt[y+1][x+2]==3) &&
                    PoleGraczaInt[y+1][x+1]==2 && PoleGraczaInt[y+1][x]==2 && PoleGraczaInt[y+1][x-1]==2)
                            {
                                PoleGraczaInt[y][x-2]=3;
                                PoleGraczaInt[y][x-1]=3;
                                PoleGraczaInt[y][x]=3;
                                PoleGraczaInt[y][x+1]=3;
                                PoleGraczaInt[y][x+2]=3;
                                PoleGraczaInt[y+1][x+2]=3;
                                PoleGraczaInt[y+2][x+2]=3;
                                PoleGraczaInt[y+2][x+1]=3;
                                PoleGraczaInt[y+2][x]=3;
                                PoleGraczaInt[y+2][x-1]=3;
                                PoleGraczaInt[y+2][x-2]=3;
                                PoleGraczaInt[y+1][x-2]=3;
                               
                                PoleGracza[x][y].setBackground(Color.BLACK);
                                PoleGracza[x-1][y].setBackground(Color.BLACK);
                                PoleGracza[x-2][y].setBackground(Color.BLACK);
                            }
        }
    }
    
    public static void algorytm_pomocniczy2_z_dolu(int x, int y)
    {
    	if(x>1)
        {
            if (PoleGraczaInt[y+1][x+1]==2 && PoleGraczaInt[y+1][x]==2 && PoleGraczaInt[y+1][x-1]==2)
                            {
                                PoleGraczaInt[y][x-2]=3;
                                PoleGraczaInt[y][x-1]=3;
                                PoleGraczaInt[y][x]=3;
                                PoleGraczaInt[y][x+1]=3;
                                PoleGraczaInt[y][x+2]=3;
                                PoleGraczaInt[y+2][x+2]=3;
                                PoleGraczaInt[y+2][x+1]=3;
                                PoleGraczaInt[y+2][x]=3;
                                PoleGraczaInt[y+2][x-1]=3;
                                PoleGraczaInt[y+2][x-2]=3;
                             
                            }
        }
    }
   
    public static void sprawdz_zatopienie_trzymasztowca_trafiony_z_prawej(int x, int y)
    {
        if(y>1)
        {
        if ((PoleGraczaInt[y-2][x+1]==0 || PoleGraczaInt[y-2][x+1]==3) && (PoleGraczaInt[y+2][x+1]==0 || PoleGraczaInt[y+2][x+1]==3) &&
                 PoleGraczaInt[y+1][x+1]==2 && PoleGraczaInt[y][x+1]==2 && PoleGraczaInt[y-1][x+1]==2) // Zatapianie dwumasztowca w lewo
                        {
                            PoleGraczaInt[y-2][x]=3;
                            PoleGraczaInt[y-2][x+1]=3;
                            PoleGraczaInt[y-2][x+2]=3;
                            PoleGraczaInt[y-1][x+2]=3;
                            PoleGraczaInt[y][x+2]=3;
                            PoleGraczaInt[y+1][x+2]=3;
                            PoleGraczaInt[y+2][x+2]=3;
                            PoleGraczaInt[y+2][x+1]=3;
                            PoleGraczaInt[y+2][x]=3;
                            PoleGraczaInt[y+1][x]=3;
                            PoleGraczaInt[y][x]=3;
                            PoleGraczaInt[y-1][x]=3;
                           
                            PoleGracza[x][y].setBackground(Color.BLACK);
                            PoleGracza[x][y-1].setBackground(Color.BLACK);
                            PoleGracza[x][y-2].setBackground(Color.BLACK);
                        }
        }
    }
    
    public static void algorytm_pomocniczy2_z_prawej(int x, int y)
    {
    	if(y>1)
        {
        if (PoleGraczaInt[y+1][x+1]==2 && PoleGraczaInt[y][x+1]==2 && PoleGraczaInt[y-1][x+1]==2) // Zatapianie dwumasztowca w lewo
                        {
                            PoleGraczaInt[y-2][x]=3;
                            PoleGraczaInt[y-2][x+2]=3;
                            PoleGraczaInt[y-1][x+2]=3;
                            PoleGraczaInt[y][x+2]=3;
                            PoleGraczaInt[y+1][x+2]=3;
                            PoleGraczaInt[y+2][x+2]=3;
                            PoleGraczaInt[y+2][x]=3;
                            PoleGraczaInt[y+1][x]=3;
                            PoleGraczaInt[y][x]=3;
                            PoleGraczaInt[y-1][x]=3;
                        }
        }
    }
   
    
    public static void algorytmy_pomocnicze(int x, int y)
    {
    	algorytm_pomocniczy1(x,y);
    	algorytm_pomocniczy1_w_lewo(x, y);
    	algorytm_pomocniczy1_w_prawo(x, y);
    	algorytm_pomocniczy1_w_dol(x, y);
    	algorytm_pomocniczy1_w_gore(x, y);
    	algorytm_pomocniczy2_w_srodku_pionowo(x, y);
    	algorytm_pomocniczy2_w_srodku_poziomo(x, y);
    	algorytm_pomocniczy2_w_gore(x, y);
    	algorytm_pomocniczy2_w_lewo(x, y);
    	algorytm_pomocniczy2_z_dolu(x, y);
    	algorytm_pomocniczy2_z_prawej(x, y);
    }
    public static void sprawdz_zatopienie_trzymasztowca_poziomo(int x, int y)
    {
        sprawdz_zatopienie_trzymasztowca_trafiony_w_srodku_poziomo(x,y);
        sprawdz_zatopienie_trzymasztowca_trafiony_z_lewej(x,y);
        sprawdz_zatopienie_trzymasztowca_trafiony_z_prawej(x,y);
    }
   
    public static void sprawdz_zatopienie_trzymasztowca_pionowo(int x, int y)
    {
        sprawdz_zatopienie_trzymasztowca_trafiony_w_srodku_pionowo(x,y);
        sprawdz_zatopienie_trzymasztowca_trafiony_z_gory(x,y);
        sprawdz_zatopienie_trzymasztowca_trafiony_z_dolu(x,y);
    }
   
    public static void sprawdz_zatopienie_czteromasztowca_trafiony_z_dolu(int x, int y)
    {
        if(x>1 && x<9)
        {
        if ((PoleGraczaInt[y+1][x-2]==0 || PoleGraczaInt[y+1][x-2]==3) && (PoleGraczaInt[y+1][x+3]==0 || PoleGraczaInt[y+1][x+3]==3) &&
                 PoleGraczaInt[y+1][x+1]==2  && PoleGraczaInt[y+1][x+2]==2 && PoleGraczaInt[y+1][x]==2 && PoleGraczaInt[y+1][x-1]==2) // Zatapianie dwumasztowca w lewo
                        {
                            PoleGraczaInt[y][x-2]=3;
                            PoleGraczaInt[y][x-1]=3;
                            PoleGraczaInt[y][x]=3;
                            PoleGraczaInt[y][x+1]=3;
                            PoleGraczaInt[y][x+2]=3;
                            PoleGraczaInt[y][x+3]=3;
                            PoleGraczaInt[y+1][x+3]=3;
                            PoleGraczaInt[y+2][x+3]=3;
                            PoleGraczaInt[y+2][x+2]=3;
                            PoleGraczaInt[y+2][x+1]=3;
                            PoleGraczaInt[y+2][x]=3;
                            PoleGraczaInt[y+2][x-1]=3;
                            PoleGraczaInt[y+2][x-2]=3;
                            PoleGraczaInt[y+1][x-2]=3;
                           
                            PoleGracza[x][y].setBackground(Color.BLACK);
                            PoleGracza[x+1][y].setBackground(Color.BLACK);
                            PoleGracza[x-1][y].setBackground(Color.BLACK);
                            PoleGracza[x-2][y].setBackground(Color.BLACK);
                        }
        }
    }
   
    public static void sprawdz_zatopienie_czteromasztowca_trafiony_z_dolu_dolu(int x, int y)
    {
        if(x>2)
        {
        if ((PoleGraczaInt[y+1][x-3]==0 || PoleGraczaInt[y+1][x-3]==3) && (PoleGraczaInt[y+1][x+2]==0 || PoleGraczaInt[y+1][x+2]==3) &&
                 PoleGraczaInt[y+1][x+1]==2  && PoleGraczaInt[y+1][x]==2 && PoleGraczaInt[y+1][x-1]==2 && PoleGraczaInt[y+1][x-2]==2) // Zatapianie dwumasztowca w lewo
                        {
                            PoleGraczaInt[y][x-3]=3;
                            PoleGraczaInt[y][x-2]=3;
                            PoleGraczaInt[y][x-1]=3;
                            PoleGraczaInt[y][x]=3;
                            PoleGraczaInt[y][x+1]=3;
                            PoleGraczaInt[y][x+2]=3;
                            PoleGraczaInt[y+1][x+2]=3;
                            PoleGraczaInt[y+2][x+2]=3;
                            PoleGraczaInt[y+2][x+1]=3;
                            PoleGraczaInt[y+2][x]=3;
                            PoleGraczaInt[y+2][x-1]=3;
                            PoleGraczaInt[y+2][x-2]=3;
                            PoleGraczaInt[y+2][x-3]=3;
                            PoleGraczaInt[y+1][x-3]=3;
                           
                            PoleGracza[x][y].setBackground(Color.BLACK);
                            PoleGracza[x-1][y].setBackground(Color.BLACK);
                            PoleGracza[x-2][y].setBackground(Color.BLACK);
                            PoleGracza[x-3][y].setBackground(Color.BLACK);
                        }
        }
    }
   
    public static void sprawdz_zatopienie_czteromasztowca_trafiony_z_gory(int x, int y)
    {
        if(x>0 && x<8)
        {
        if ((PoleGraczaInt[y+1][x-1]==0 || PoleGraczaInt[y+1][x-1]==3) && (PoleGraczaInt[y+1][x+4]==0 || PoleGraczaInt[y+1][x+4]==3) &&
                 PoleGraczaInt[y+1][x+1]==2  && PoleGraczaInt[y+1][x]==2 && PoleGraczaInt[y+1][x+2]==2 && PoleGraczaInt[y+1][x+3]==2) // Zatapianie dwumasztowca w lewo
                        {
                            PoleGraczaInt[y][x-1]=3;
                            PoleGraczaInt[y][x]=3;
                            PoleGraczaInt[y][x+1]=3;
                            PoleGraczaInt[y][x+2]=3;
                            PoleGraczaInt[y][x+3]=3;
                            PoleGraczaInt[y][x+4]=3;
                            PoleGraczaInt[y+1][x+4]=3;
                            PoleGraczaInt[y+2][x+4]=3;
                            PoleGraczaInt[y+2][x+3]=3;
                            PoleGraczaInt[y+2][x+2]=3;
                            PoleGraczaInt[y+2][x+1]=3;
                            PoleGraczaInt[y+2][x]=3;
                            PoleGraczaInt[y+2][x-1]=3;
                            PoleGraczaInt[y+1][x-1]=3;
                           
                            PoleGracza[x][y].setBackground(Color.BLACK);
                            PoleGracza[x-1][y].setBackground(Color.BLACK);
                            PoleGracza[x+1][y].setBackground(Color.BLACK);
                            PoleGracza[x+2][y].setBackground(Color.BLACK);
                        }
        }
    }
   
    public static void sprawdz_zatopienie_czteromasztowca_trafiony_z_gory_gory(int x, int y)
    {
        if(x<7)
        {
        if ((PoleGraczaInt[y+1][x]==0 || PoleGraczaInt[y+1][x]==3) && (PoleGraczaInt[y+1][x+5]==0 || PoleGraczaInt[y+1][x+5]==3) &&
                 PoleGraczaInt[y+1][x+1]==2  && PoleGraczaInt[y+1][x+2]==2 && PoleGraczaInt[y+1][x+3]==2 && PoleGraczaInt[y+1][x+4]==2) // Zatapianie dwumasztowca w lewo
                        {
                            PoleGraczaInt[y][x]=3;
                            PoleGraczaInt[y][x+1]=3;
                            PoleGraczaInt[y][x+2]=3;
                            PoleGraczaInt[y][x+3]=3;
                            PoleGraczaInt[y][x+4]=3;
                            PoleGraczaInt[y][x+5]=3;
                            PoleGraczaInt[y+1][x+5]=3;
                            PoleGraczaInt[y+2][x+5]=3;
                            PoleGraczaInt[y+2][x+4]=3;
                            PoleGraczaInt[y+2][x+3]=3;
                            PoleGraczaInt[y+2][x+2]=3;
                            PoleGraczaInt[y+2][x+1]=3;
                            PoleGraczaInt[y+2][x]=3;
                            PoleGraczaInt[y+1][x]=3;
                           
                            PoleGracza[x][y].setBackground(Color.BLACK);
                            PoleGracza[x+1][y].setBackground(Color.BLACK);
                            PoleGracza[x+2][y].setBackground(Color.BLACK);
                            PoleGracza[x+3][y].setBackground(Color.BLACK);
                        }
        }
    }
   
   
   
    public static void sprawdz_zatopienie_czteromasztowca_pionowo(int x, int y)
    {
        sprawdz_zatopienie_czteromasztowca_trafiony_z_dolu(x,y);
        sprawdz_zatopienie_czteromasztowca_trafiony_z_dolu_dolu(x,y);
        sprawdz_zatopienie_czteromasztowca_trafiony_z_gory(x,y);
        sprawdz_zatopienie_czteromasztowca_trafiony_z_gory_gory(x,y);
    }
   
   
   
    public static void sprawdz_zatopienie_czteromasztowca_trafiony_z_lewej(int x, int y)
    {
        if(y>1 && y<8)
        {
        if ((PoleGraczaInt[y-1][x+1]==0 || PoleGraczaInt[y-1][x+1]==3) && (PoleGraczaInt[y+4][x+1]==0 || PoleGraczaInt[y+4][x+1]==3) &&
                PoleGraczaInt[y+1][x+1]==2 && PoleGraczaInt[y][x+1]==2 && PoleGraczaInt[y+2][x+1]==2 && PoleGraczaInt[y+3][x+1]==2) // Zatapianie dwumasztowca w lewo
                        {
                            PoleGraczaInt[y-1][x]=3;
                            PoleGraczaInt[y-1][x+1]=3;
                            PoleGraczaInt[y-1][x+2]=3;
                            PoleGraczaInt[y][x+2]=3;
                            PoleGraczaInt[y+1][x+2]=3;
                            PoleGraczaInt[y+2][x+2]=3;
                            PoleGraczaInt[y+3][x+2]=3;
                            PoleGraczaInt[y+4][x+2]=3;
                            PoleGraczaInt[y+4][x+1]=3;
                            PoleGraczaInt[y+4][x]=3;
                            PoleGraczaInt[y+3][x]=3;
                            PoleGraczaInt[y+2][x]=3;
                            PoleGraczaInt[y+1][x]=3;
                            PoleGraczaInt[y][x]=3;
                           
                            PoleGracza[x][y].setBackground(Color.BLACK);
                            PoleGracza[x][y-1].setBackground(Color.BLACK);
                            PoleGracza[x][y+1].setBackground(Color.BLACK);
                            PoleGracza[x][y+2].setBackground(Color.BLACK);
                         
                        }
        }
    }
   
    public static void sprawdz_zatopienie_czteromasztowca_trafiony_z_lewej_lewej(int x, int y)
    {
        if(y<7)
        {
        if ((PoleGraczaInt[y][x+1]==0 || PoleGraczaInt[y][x+1]==3) && (PoleGraczaInt[y+5][x+1]==0 || PoleGraczaInt[y+5][x+1]==3) &&
                PoleGraczaInt[y+1][x+1]==2 && PoleGraczaInt[y+2][x+1]==2 && PoleGraczaInt[y+3][x+1]==2 && PoleGraczaInt[y+4][x+1]==2) // Zatapianie dwumasztowca w lewo
                        {
                            PoleGraczaInt[y][x]=3;
                            PoleGraczaInt[y][x+1]=3;
                            PoleGraczaInt[y][x+2]=3;
                            PoleGraczaInt[y+1][x+2]=3;
                            PoleGraczaInt[y+2][x+2]=3;
                            PoleGraczaInt[y+3][x+2]=3;
                            PoleGraczaInt[y+4][x+2]=3;
                            PoleGraczaInt[y+5][x+2]=3;
                            PoleGraczaInt[y+5][x+1]=3;
                            PoleGraczaInt[y+5][x]=3;
                            PoleGraczaInt[y+4][x]=3;
                            PoleGraczaInt[y+3][x]=3;
                            PoleGraczaInt[y+2][x]=3;
                            PoleGraczaInt[y+1][x]=3;
                           
                            PoleGracza[x][y].setBackground(Color.BLACK);
                            PoleGracza[x][y+1].setBackground(Color.BLACK);
                            PoleGracza[x][y+2].setBackground(Color.BLACK);
                            PoleGracza[x][y+3].setBackground(Color.BLACK);
                        }
        }
    }
       
    public static void sprawdz_zatopienie_czteromasztowca_trafiony_z_prawej(int x, int y)
    {
        if(y>1 && y<9)
        {
        if ((PoleGraczaInt[y-2][x+1]==0 || PoleGraczaInt[y-2][x+1]==3) && (PoleGraczaInt[y+3][x+1]==0 || PoleGraczaInt[y+3][x+1]==3) &&
                PoleGraczaInt[y+1][x+1]==2 && PoleGraczaInt[y+2][x+1]==2 && PoleGraczaInt[y][x+1]==2 && PoleGraczaInt[y-1][x+1]==2) // Zatapianie dwumasztowca w lewo
                        {
                            PoleGraczaInt[y-2][x]=3;
                            PoleGraczaInt[y-2][x+1]=3;
                            PoleGraczaInt[y-2][x+2]=3;
                            PoleGraczaInt[y-1][x+2]=3;
                            PoleGraczaInt[y][x+2]=3;
                            PoleGraczaInt[y+1][x+2]=3;
                            PoleGraczaInt[y+2][x+2]=3;
                            PoleGraczaInt[y+3][x+2]=3;
                            PoleGraczaInt[y+3][x+1]=3;
                            PoleGraczaInt[y+3][x]=3;
                            PoleGraczaInt[y+2][x]=3;
                            PoleGraczaInt[y+1][x]=3;
                            PoleGraczaInt[y][x]=3;
                            PoleGraczaInt[y-1][x]=3;
                           
                            PoleGracza[x][y].setBackground(Color.BLACK);
                            PoleGracza[x][y+1].setBackground(Color.BLACK);
                            PoleGracza[x][y-1].setBackground(Color.BLACK);
                            PoleGracza[x][y-2].setBackground(Color.BLACK);
                        }
        }
    }
   
    public static void sprawdz_zatopienie_czteromasztowca_trafiony_z_prawej_prawej(int x, int y)
    {
        if(y>2)
        {
        if ((PoleGraczaInt[y-3][x+1]==0 || PoleGraczaInt[y-3][x+1]==3) && (PoleGraczaInt[y+2][x+1]==0 || PoleGraczaInt[y+2][x+1]==3) &&
                PoleGraczaInt[y+1][x+1]==2 && PoleGraczaInt[y][x+1]==2 && PoleGraczaInt[y-1][x+1]==2 && PoleGraczaInt[y-2][x+1]==2) // Zatapianie dwumasztowca w lewo
                        {
                            PoleGraczaInt[y-3][x]=3;
                            PoleGraczaInt[y-3][x+1]=3;
                            PoleGraczaInt[y-3][x+2]=3;
                            PoleGraczaInt[y-2][x+2]=3;
                            PoleGraczaInt[y-1][x+2]=3;
                            PoleGraczaInt[y][x+2]=3;
                            PoleGraczaInt[y+1][x+2]=3;
                            PoleGraczaInt[y+2][x+2]=3;
                            PoleGraczaInt[y+2][x+1]=3;
                            PoleGraczaInt[y+2][x]=3;
                            PoleGraczaInt[y+1][x]=3;
                            PoleGraczaInt[y][x]=3;
                            PoleGraczaInt[y-1][x]=3;
                            PoleGraczaInt[y-2][x]=3;
                           
                            PoleGracza[x][y].setBackground(Color.BLACK);
                            PoleGracza[x][y-1].setBackground(Color.BLACK);
                            PoleGracza[x][y-2].setBackground(Color.BLACK);
                            PoleGracza[x][y-3].setBackground(Color.BLACK);
                        }
        }
    }
   
    public static void sprawdz_zatopienie_czteromasztowca_poziomo(int x, int y)
    {
        sprawdz_zatopienie_czteromasztowca_trafiony_z_lewej(x,y);
        sprawdz_zatopienie_czteromasztowca_trafiony_z_lewej_lewej(x,y);
        sprawdz_zatopienie_czteromasztowca_trafiony_z_prawej(x,y);
        sprawdz_zatopienie_czteromasztowca_trafiony_z_prawej_prawej(x,y);
    }
   
   
    public static void sprawdz_koniec_gry(int tab[][])
    {
        int ilosc_trafionych_okretow=0;
        for(int i=0; i<tab.length; i++)
        {
            for(int j=0; j<tab[i].length;j++)
            {
                if(tab[i][j]==2)
                {
                    ilosc_trafionych_okretow++;
                    if(ilosc_trafionych_okretow==20)
                    {
                        if(ruch_int==0)
                        tChat.setText(tChat.getText().trim() + "\n" + "INFO: Gratulacje wygrywasz grę trafiłeś wszystkie okręty przeciwnika.");
                        if(ruch_int==1)
                            tChat.setText(tChat.getText().trim() + "\n" + "INFO: Przegrywasz grę przeciwnik trafił wszystkie twoje okręty.");
                        ruch_int=10; // blokada ruchow
                    }
                }
            }
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
           
            if(source==vsGracz)
            {
                rodzaj_okna++;
                uruchomSerwer();
                dispose();
               
               
            }
            if(source==vsKomputer)
            {
                rodzaj_okna++;
                komputer();
                dispose();
               
            }
            if(source==wyjscie2)
            {
                dispose();
            }
           
       
       
            if(source == bWyslij)
            {
               
                String msgout="";
                try{
                    msgout=tWprowadz.getText().trim();
                    output.writeUTF("100"); // 100 - wiadomosc z chatu - rodzaj info
                    output.writeUTF("Server: "+msgout);
                    System.out.println("wysylam");
                    tChat.setText(tChat.getText().trim() + "\n" + "Server: " + msgout );
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
                        output.writeUTF("500"); // 500 - Gracz resetuje swoj¹ plansze
                    }
                    catch(Exception exception)
                    {
                        System.out.println(exception.getMessage());
                    }
                    tChat.setText(tChat.getText().trim() + "\n" + "PODPOWIEDŹ: Ustaw teraz okręt 4-masztowy");
                    ilosc=0;
                    pierwsze_ustawienie=0;
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
            if(ilosc==19){ // Jeli gracz ustwi³ wszystkie okrêty
                System.out.println("Ustawiles już wszystkie okręty"); // Komunikat w konsoli
                try{
                    output.writeUTF("100"); // 100 - wiadomosc z chatu - rodzaj info
                    output.writeUTF("INFO: Server ustawił wszystkie okręty"); // Wys³anie informacji do klienta
                    System.out.println("wysylam");
                    }
                catch(Exception exception)
                {
                System.out.println(exception.getMessage());
                }
               
                tChat.setText(tChat.getText().trim() + "\n" + "INFO: Ustawileś już wszystkie okręty");
               
                if(ilosc_przeciwnik_int==20)
                {
                    tChat.setText(tChat.getText().trim() + "\n" + "INFO: Aby rozpocząć grę spróbuj trafić okręt przeciwnika");
                }
               
               
              }
           
           
           
            try {
            // Ustawienie okrętów na własnej planszy
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
                            if(PoleGraczaInt[j][i]==1) // Zabezpieczenie przed ponownym klikniêciem tego samego buttona
                            {
                                ilosc--;
                            }
                           
                            PoleGracza[i][j].setBackground(Color.RED);
                            PoleGraczaInt[j+1][i+1]=1; // Nawi¹zanie do tablicy int ktora ma informacje o zaznaczonych polach tablicy
                            ilosc++;
                           
                            try {
                                output.writeUTF("200"); // 200 - Wiadomosc z gry rodzaj info
                                output.writeUTF(""+(j)); // WSPÓ£ X
                                output.writeUTF(""+(i)); // WSPÓ£ Y
                                }
                                catch(Exception exception)
                                {
                                System.out.println(exception.getMessage());
                                }
                            System.out.print("Kliknieto przcisk o współrzędnych: X = "+j );// Komunikat w konsoli
                            System.out.println(" Y = "+i);// Komunikat w konsoli
                        }
                           
                           
                           
                        }
                    if(pierwsze_ustawienie==0 && ilosc==0 && source == PoleGracza[i][j]) // *** PIERWSZE USTAWIENIE ***
                    {
                        PoleGracza[i][j].setBackground(Color.RED);
                        PoleGraczaInt[j+1][i+1]=1; // Nawi¹zanie do tablicy int ktora ma informacje o zaznaczonych polach tablicy
                        ilosc++;
                        pierwsze_ustawienie++;
                        try {
                            output.writeUTF("200"); // 200 - Wiadomosc z gry rodzaj info
                            output.writeUTF(""+(j)); // WSPÓ£ X
                            output.writeUTF(""+(i)); // WSPÓ£ Y
                            }
                            catch(Exception exception)
                            {
                            System.out.println(exception.getMessage());
                            }
                        System.out.print("Kliknieto przcisk o współrzędnych: X = "+j );// Komunikat w konsoli
                        System.out.println(" Y = "+i);// Komunikat w konsoli
                    }
                    }
                   
                System.out.println(ilosc);
                }
            }
            catch(ArrayIndexOutOfBoundsException ex)
            {
            System.out.println(ex);
           
            }
           
           
            if(ilosc==20 && ilosc_przeciwnik_int==20 && ruch_int==0) // Sprawdzenie czy gracz i przeciwnik ustawi³ wszystkie swoje okrêty aby móc rozpocz¹æ grê
            {
                // Sprawdzenie czy trafilismy okrêt
                for(int i=0; i<PolePrzeciwnika.length; i++)
                {
                    for(int j=0; j<PolePrzeciwnika[i].length;j++)
                    {
                       
                        if(source == PolePrzeciwnika[i][j]) // Jeli zosta³ klikniety przycisk
                            {
                           
                            if(PolePrzeciwnikaInt[j+1][i+1]==1) //Jesli == 1 oznacza ¿e trafilimy okrêt
                                {
                                    PolePrzeciwnikaInt[j+1][i+1]=2; // Oznaczenie okrêtu jako trafiony pomocne przy sprawdzeniu czy dany okrêt jest zatopiony
                                 
                                 // ZATOPIENIE - SPRAWDZANIE
                                   
                                   
                                    if(PolePrzeciwnikaInt[j+1][i+1]==2 && PolePrzeciwnikaInt[j+2][i+1]==0 && PolePrzeciwnikaInt[j][i+1]==0 && PolePrzeciwnikaInt[j+1][i+2]==0 && PolePrzeciwnikaInt[j+1][i]==0)   // OKRÊT JEDNO MASZTOWY  
                                        {
                                             PolePrzeciwnika[i][j].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                        }
                                    else if(PolePrzeciwnikaInt[j+1][i+1]==2 && PolePrzeciwnikaInt[j][i+1]==2 && PolePrzeciwnikaInt[j-1][i+1]==0 && PolePrzeciwnikaInt[j+2][i+1]==0) // OKRET DWUMASZTOWY POZIOM w PRAWO
                                        {
                                            PolePrzeciwnika[i][j].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                            PolePrzeciwnika[i][j-1].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                        }
                                    else if(PolePrzeciwnikaInt[j+1][i+1]==2 && PolePrzeciwnikaInt[j+2][i+1]==2 && PolePrzeciwnikaInt[j][i+1]==0 && PolePrzeciwnikaInt[j+3][i+1]==0) // OKRET DWUMASZTOWY POZIOM w LEWO
                                    {
                                        PolePrzeciwnika[i][j].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                        PolePrzeciwnika[i][j+1].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                    }
                                    else if(PolePrzeciwnikaInt[j+1][i+1]==2 && PolePrzeciwnikaInt[j+1][i+2]==2 && PolePrzeciwnikaInt[j+1][i+3]==0 && PolePrzeciwnikaInt[j+1][i]==0) // OKRET DWUMASZTOWY PION w DÓ£
                                    {
                                        PolePrzeciwnika[i][j].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                        PolePrzeciwnika[i+1][j].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                    }
                                    else if(PolePrzeciwnikaInt[j+1][i+1]==2 && PolePrzeciwnikaInt[j+1][i]==2 && PolePrzeciwnikaInt[j+1][i-1]==0 && PolePrzeciwnikaInt[j+1][i+2]==0) // OKRET DWUMASZTOWY PION w GÓRE
                                    {
                                        PolePrzeciwnika[i][j].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                        PolePrzeciwnika[i-1][j].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                    }
                                    else if(PolePrzeciwnikaInt[j+1][i+1]==2 && PolePrzeciwnikaInt[j+2][i+1]==2 && PolePrzeciwnikaInt[j+3][i+1]==2 && PolePrzeciwnikaInt[j+4][i+1]==0 && PolePrzeciwnikaInt[j][i+1]==0) // OKRET TRZYMASZTOWY POZIOM w PRAWO
                                    {
                                        PolePrzeciwnika[i][j].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                        PolePrzeciwnika[i][j+1].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                        PolePrzeciwnika[i][j+2].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                    }
                                    else if(PolePrzeciwnikaInt[j+1][i+1]==2 && PolePrzeciwnikaInt[j][i+1]==2 && PolePrzeciwnikaInt[j-1][i+1]==2 && PolePrzeciwnikaInt[j-3][i+1]==0 && PolePrzeciwnikaInt[j+2][i+1]==0) // OKRET TRZYMASZTOWY POZIOM w LEWO
                                    {
                                        PolePrzeciwnika[i][j].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                        PolePrzeciwnika[i][j-1].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                        PolePrzeciwnika[i][j-2].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                    }
                                    else if(PolePrzeciwnikaInt[j+1][i+1]==2 && PolePrzeciwnikaInt[j+1][i+2]==2 && PolePrzeciwnikaInt[j+1][i+3]==2 && PolePrzeciwnikaInt[j+1][i+4]==0 && PolePrzeciwnikaInt[j+1][i]==0) // OKRET TRZYMASZTOWY PION w DÓ£
                                    {
                                        PolePrzeciwnika[i][j].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                        PolePrzeciwnika[i+1][j].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                        PolePrzeciwnika[i+2][j].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                    }
                                    else if(PolePrzeciwnikaInt[j+1][i+1]==2 && PolePrzeciwnikaInt[j+1][i]==2 && PolePrzeciwnikaInt[j+1][i-1]==2 && PolePrzeciwnikaInt[j+1][i-2]==0 && PolePrzeciwnikaInt[j+1][i+2]==0) // OKRET TRZYMASZTOWY PION w GÓRÊ
                                    {
                                        PolePrzeciwnika[i][j].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                        PolePrzeciwnika[i-1][j].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                        PolePrzeciwnika[i-2][j].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                    }
                                    else if(PolePrzeciwnikaInt[j+1][i+1]==2 && PolePrzeciwnikaInt[j+2][i+1]==2 && PolePrzeciwnikaInt[j+3][i+1]==2 && PolePrzeciwnikaInt[j+4][i+1]==2 && PolePrzeciwnikaInt[j+5][i+1]==0 && PolePrzeciwnikaInt[j][i+1]==0) // OKRET CZTEROMASZTOWY POZIOM w PRAWO
                                    {
                                        PolePrzeciwnika[i][j].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                        PolePrzeciwnika[i][j+1].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                        PolePrzeciwnika[i][j+2].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                        PolePrzeciwnika[i][j+3].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                    }
                                    else if(PolePrzeciwnikaInt[j+1][i+1]==2 && PolePrzeciwnikaInt[j][i+1]==2 && PolePrzeciwnikaInt[j-1][i+1]==2 && PolePrzeciwnikaInt[j-2][i+1]==2 && PolePrzeciwnikaInt[j-3][i+1]==0 && PolePrzeciwnikaInt[j+2][i+1]==0) // OKRET CZTEROMASZTOWY POZIOM w LEWO
                                    {
                                        PolePrzeciwnika[i][j].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                        PolePrzeciwnika[i][j-1].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                        PolePrzeciwnika[i][j-2].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                        PolePrzeciwnika[i][j-3].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
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
                                        PolePrzeciwnika[i][j].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                        PolePrzeciwnika[i-1][j].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                        PolePrzeciwnika[i-2][j].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                        PolePrzeciwnika[i-3][j].setBackground(Color.BLACK); // Okrêt zosta³ zatopiony
                                    }
                                 
                                    else
                                        PolePrzeciwnika[i][j].setBackground(Color.GREEN); // Okrêt zosta³ trafiony
                                   
                                   
                                   
                               
                                    sprawdz_koniec_gry(PolePrzeciwnikaInt);
                                   
                                    try{
                                        output.writeUTF("100"); // 100 - wiadomosc z chatu - rodzaj info
                                        output.writeUTF("INFO: Server trafił  twój okręt"); // Wys³anie informacji do klienta o trafieniu
                                        tChat.setText(tChat.getText().trim() + "\n" + "INFO: Trafiłeś okręt przeciwnika");
                                        output.writeUTF("300"); // 300 - Wiadomosc z gry rodzaj info - wspólrzedne z pola gry przeciwnika - 300 TRAFIONY
                                        output.writeUTF(""+j); // WSPÓ£ X
                                        output.writeUTF(""+i); // WSPÓ£ Y
                                        }
                                        catch(Exception exception)
                                        {
                                            System.out.println(exception.getMessage());
                                        }
                                    moje_punkty++;
                                   
                                }
                                if(PolePrzeciwnikaInt[j+1][i+1]==0)  //Jesli == 0 oznacza, ¿e nie trafilimy okrêtu i musimy oddaæ ruch
                                {
                                   
                                    PolePrzeciwnika[i][j].setBackground(Color.BLUE); // Okrêt nie zosta³ trafiony
                                    if(pierwsze_ustewienie_komputera==1) // Wykorzystywane przy grze z komputerem
                                    {
                                        ruch_int=1;
                                        komputer();
                                    }
                                   
                                    try{
                                        output.writeUTF("100"); // 100 - wiadomosc z chatu - rodzaj info
                                        output.writeUTF("INFO: Server nie trafił twojego okrętu"); // Wys³anie informacji do klienta o trafieniu
                                        tChat.setText(tChat.getText().trim() + "\n" + "INFO: Nie trafiłeś okrętu przeciwnika");
                                        output.writeUTF("400"); // 400 - Wiadomosc z gry rodzaj info - wspólrzedne z pola gry przeciwnika - 400 NIE TRAFIONY
                                        output.writeUTF(""+j); // WSPÓ£ X
                                        output.writeUTF(""+i); // WSPÓ£ Y
                                        ruch_int=1; // Jeli ruch=1 to kolejke ma klient
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
                                        output.writeUTF("INFO: Server wygrywa trafił wszystkie okręty"); // Wys³anie informacji do klienta
                                        tChat.setText(tChat.getText().trim() + "\n" + "INFO: Wygrałeś gre trafiłeś wszystkie okręty");
                                        ruch_int=2; // KONIEC GRY ZABLOKOWANIE RUCHÓW
                                        output.writeUTF("700"); // Do kolejnoci gry
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