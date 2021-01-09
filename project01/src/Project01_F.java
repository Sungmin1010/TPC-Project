import javax.swing.*;
import java.awt.*;

public class Project01_F {

    JTextField address;
    JLabel resAddress, resX, resY, jibunAddress, imageLabel; //NaverMap에서 사용
    public void initGUI(){
        JFrame frame = new JFrame("Map View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = frame.getContentPane();
        imageLabel = new JLabel("지도보기");

        JPanel panel = new JPanel();
        JLabel addressLbl = new JLabel("주소입력");
        address = new JTextField(50);
        JButton btn = new JButton("클릭");
        panel.add(addressLbl);
        panel.add(address);
        panel.add(btn);
        btn.addActionListener(new NaverMap(this));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(4, 1));
        resAddress = new JLabel("도로명");
        jibunAddress = new JLabel("지번주소");
        resX = new JLabel("경도");
        resY = new JLabel("위도");
        bottomPanel.add(resAddress);
        bottomPanel.add(jibunAddress);
        bottomPanel.add(resX);
        bottomPanel.add(resY);

        container.add(BorderLayout.NORTH, panel);
        container.add(BorderLayout.CENTER, imageLabel);
        container.add(BorderLayout.SOUTH, bottomPanel);

        frame.setSize(730, 660);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Project01_F().initGUI();
    }
}
