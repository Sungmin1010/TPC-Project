package kr.blue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Project02_C extends JFrame implements ActionListener, ItemListener {

    private Choice chyear, chmonth;
    private JLabel yLabel, mLabel;
    private JTextArea area;
    GregorianCalendar gc;
    private int year, month;
    private JLabel[] dayLable = new JLabel[7];
    private String[] day = {"일", "월", "화", "수", "목", "금", "토"};
    private JButton[] days = new JButton[42];
    private JPanel selectPanel = new JPanel();
    private GridLayout grid = new GridLayout(7, 7, 5, 5);
    private Calendar ca = Calendar.getInstance();
    private Dimension dimen, dimen1;
    private int xpos, ypos;

    public Project02_C(){
        setTitle("오늘의 QT : "+ca.get(Calendar.YEAR) + "/" + (ca.get(Calendar.MONTH)+1) + "/" + ca.get(Calendar.DATE));
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dimen = Toolkit.getDefaultToolkit().getScreenSize();
        dimen1 = this.getSize();
        xpos = (int)(dimen.getWidth() / 2 - dimen1.getWidth()/2);
        ypos = (int)(dimen.getHeight() / 2 - dimen1.getHeight()/2);
        setLocation(xpos, ypos);
        setResizable(false);
        setVisible(true);
        chyear = new Choice(); chmonth = new Choice();
        yLabel = new JLabel("년"); mLabel = new JLabel("월");
        init();
    }

    public void init(){
        select();
        calendar();
    }

    public void select(){
        JPanel panel = new JPanel(grid);
        for(int i=2021; i>= 2000; i--){
            chyear.add(String.valueOf(i));
        }
        for(int i=1; i<=12; i++){
            chmonth.add(String.valueOf(i));
        }
        for(int i=0; i<day.length; i++){
            dayLable[i] = new JLabel(day[i], JLabel.CENTER);
            panel.add(dayLable[i]);
            dayLable[i].setBackground(Color.GRAY);
        }
        dayLable[6].setForeground(Color.BLUE);
        dayLable[0].setForeground(Color.RED);
        for(int i=0; i< 42; i++){
            days[i] = new JButton("");
            if(i % 7 ==0)
                days[i].setForeground(Color.RED);
            else if(i % 7 == 6)
                days[i].setForeground(Color.BLUE);
            else
                days[i].setForeground(Color.BLACK);
            days[i].addActionListener(this);
            panel.add(days[i]);
        }
        selectPanel.add(chyear);
        selectPanel.add(yLabel);
        selectPanel.add(chmonth);
        selectPanel.add(mLabel);
        area = new JTextArea(60, 40);
        area.setCaretPosition(area.getDocument().getLength());
        JScrollPane scrollPane = new JScrollPane(area);
        this.add(selectPanel, "North");
        this.add(panel, "Center");
        this.add(scrollPane, "East");

        String m = (ca.get(Calendar.MONTH) + 1)+"";
        String y = ca.get(Calendar.YEAR)+ "";
        chyear.select(y);
        chmonth.select(m);
        chyear.addItemListener(this);
        chmonth.addItemListener(this);
    }

    public void calendar(){
        year = Integer.parseInt(chyear.getSelectedItem());
        month = Integer.parseInt(chmonth.getSelectedItem());
        gc = new GregorianCalendar(year, month-1, 1);
        int max = gc.getActualMaximum(gc.DAY_OF_MONTH);
        int week = gc.get(gc.DAY_OF_WEEK);
        System.out.println("DAY_OF_WEEK : " + week);
        String today = Integer.toString(ca.get(Calendar.DATE));
        String today_month = Integer.toString(ca.get(Calendar.MONTH) + 1);
        System.out.println("today : " + today);
        for(int i = 0; i < days.length; i++){
            days[i].setEnabled(true);
        }
        for(int i=0; i<week-1; i++){
            days[i].setEnabled(false);
        }
        for(int i=week; i<max+week; i++){
            days[i-1].setText((String.valueOf(i-week+1)));
            days[i-1].setBackground(Color.WHITE);
            if(today_month.equals(String.valueOf(month))){
                if(today.equals(days[i-1].getText())){
                    days[i-1].setBackground(Color.CYAN);
                }
            }
        }
        for(int i=(max+week-1); i<days.length; i++){
            days[i].setEnabled(false);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        area.setText("");
        String year = chyear.getSelectedItem();
        String month = chmonth.getSelectedItem();
        JButton btn = (JButton)e.getSource();
        String day = btn.getText();
        System.out.println(year+", "+ month + ", "+day);
        String bible = year+"-"+month+"-"+day;
        System.out.println("bible : " + bible);
        String url = "https://sum.su.or.kr:8888/bible/today/Ajax/Bible/BodyMatter?qt_ty=QT1&Base_de="+bible+"&bibleType=1";
        System.out.println("url : " + url);
        try{
            Document doc = Jsoup.connect(url).post();
            Element bible_text = doc.select(".bible_text").first();
            System.out.println("bible_text:"+bible_text.text());
            Element bibleinfo_box = doc.select("#bibleinfo_box").first();
            System.out.println("bibleinfo_box: "+bibleinfo_box.text());

            Element dailybible_info = doc.select("#dailybible_info").first();
            System.out.println("dailybible_info : "+dailybible_info.text());
            area.append(dailybible_info.text()+"\n");
            area.append(bible_text.text()+"\n");
            area.append(bibleinfo_box.text()+"\n");

            Elements liList = doc.select(".body_list > li");
            for(Element li: liList){
                String line = li.select(".info").first().text();
                if(line.length() > 65){
                    line = line.substring(0, 36)+"\n"+line.substring(36, 66)+"\n"+line.substring(66)+"\n";
                    area.append(li.select(".num").first().text() + " : " + line);
                }else if(line.length() > 35){
                    line = line.substring(0, 36) + "\n"+line.substring(36)+"\n";
                    area.append(li.select(".num").first().text() + " : " + line);
                }else{
                    area.append(li.select(".num").first().text() + " : " + li.select(".info").first().text() + "\n");
                }
                System.out.print(li.select(".num").first().text() + " : ");
                System.out.print(li.select(".info").first().text());
                System.out.println();
            }

        }catch (Exception exception){
            exception.printStackTrace();
        }

    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Color color = this.getBackground();
        if(e.getStateChange()==ItemEvent.SELECTED){
            for(int i=0; i<42; i++){
                if( !days[i].getText().equals("")){
                    days[i].setText("");
                    days[i].setBackground(color);
                }
            }
            calendar();
        }

    }

    public static void main(String[] args) {
        new Project02_C();
    }
}
