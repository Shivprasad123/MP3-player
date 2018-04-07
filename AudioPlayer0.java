
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.event.*;
import javax.sound.sampled.*;

public class AudioPlayer0 extends JFrame{

	AudioFormat audioFormat;
	AudioInputStream audioInputStream;
	SourceDataLine sourceDataLine;
	boolean stopPlayback = false;
	final JButton stopBtn = new JButton("Stop");
	final JButton playBtn = new JButton("Play");
	JTextField textField ;
	
	String str;
	DefaultListModel<String> listModel;
	public static void main(String args[]){
		new AudioPlayer0();
	}

	public AudioPlayer0(){

		stopBtn.setEnabled(false);
		playBtn.setEnabled(true);
		Container con = getContentPane();
		JPanel pan = new JPanel();

                listModel = new DefaultListModel<String>();
                File folder = new File("/home/shivprasasd/JAVA/Project/MP3Player");
                File[] listOfFiles = folder.listFiles();
                for (int i = 0; i < listOfFiles.length; i++)
                {
                        String str = listOfFiles[i].getName();
                        if(str.contains(".au"))
                                listModel.addElement(listOfFiles[i].getName());
                }
                JList<String> list = new JList<String>(listModel);
                list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
                list.setLayoutOrientation(JList.VERTICAL);
                list.setVisibleRowCount(5);
                list.addListSelectionListener(new ListSelectionListener(){
		public void valueChanged(ListSelectionEvent e){
			JList source = (JList) e.getSource();
                	str = source.getSelectedValue().toString();
			textField = new JTextField(str);
                //System.out.println(str);

		}
});
                JScrollPane ScrollPane = new JScrollPane(list);
                pan.add(ScrollPane);


		playBtn.addActionListener(
				new ActionListener(){
				public void actionPerformed(
						ActionEvent e){
				stopBtn.setEnabled(true);
				playBtn.setEnabled(false);
				playAudio();
				}
				}
				);

		stopBtn.addActionListener(
				new ActionListener(){
				public void actionPerformed(
						ActionEvent e){
				stopPlayback = true;
				}
				}
				);
		JFrame frame = new JFrame();
		JPanel pan1 = new JPanel();
		pan1.setLayout(new FlowLayout());
//                JProgressBar bar1 = new JProgressBar();
 //             bar1.setPreferredSize(new Dimension(400,30));

		pan1.add(playBtn);
		pan1.add(stopBtn);
//pan1.add(bar1);
		con.add(pan,BorderLayout.WEST);
		con.add(pan1,BorderLayout.CENTER);
		frame.add(con);
		frame.setTitle("AUDIO PLAYER");
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setSize(550,110);
		frame.setVisible(true);
		
	}//end constructor
	private void playAudio() {
		try{
			File soundFile =
				new File(textField.getText());
			audioInputStream = AudioSystem.
				getAudioInputStream(soundFile);
			audioFormat = audioInputStream.getFormat();
			System.out.println(audioFormat);

			DataLine.Info dataLineInfo =
				new DataLine.Info(
						SourceDataLine.class,
						audioFormat);

			sourceDataLine =
				(SourceDataLine)AudioSystem.getLine(
						dataLineInfo);

			new PlayThread().start();
		}catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	class PlayThread extends Thread{
		byte tempBuffer[] = new byte[10000];

		public void run(){
			try{
				sourceDataLine.open(audioFormat);
				sourceDataLine.start();

				int cnt;
				while((cnt = audioInputStream.read(
								tempBuffer,0,tempBuffer.length)) != -1
						&& stopPlayback == false){
					if(cnt > 0){
						sourceDataLine.write(
								tempBuffer, 0, cnt);
					}
				}
				sourceDataLine.drain();
				sourceDataLine.close();

				stopBtn.setEnabled(false);
				playBtn.setEnabled(true);
				stopPlayback = false;
			}catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

}
