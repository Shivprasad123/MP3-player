import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.event.*;
import javax.sound.sampled.*;

public class AudioPlayer1 extends JFrame{

	AudioFormat audioFormat;
	AudioInputStream audioInputStream;
	SourceDataLine sourceDataLine;
	boolean stopPlayback = false;
	final JButton stopBtn = new JButton("Stop");
	final JButton playBtn = new JButton("Play");
	JButton next = new JButton("Next");
	JTextField textField ;
	JList source;
	String str;
	int n;
	String []src = new String[100];
	DefaultListModel<String> listModel;
	public static void main(String args[]){
		new AudioPlayer1();
	}

	public AudioPlayer1(){

		stopBtn.setEnabled(false);
		playBtn.setEnabled(true);
		Container con = getContentPane();
		JPanel pan = new JPanel();

                listModel = new DefaultListModel<String>();
                File folder = new File("/home/shivprasasd/JAVA/Project/MP3Player");
                File[] listOfFiles = folder.listFiles();
//		src[] = new String[lisrOfFiles.length];
		int j=0;
                for (int i = 0; i < listOfFiles.length; i++)
                {
                        String str = listOfFiles[i].getName();
                        if(str.contains(".au")){
                                listModel.addElement(listOfFiles[i].getName());
					if(listOfFiles[i].getName()!="null"){
						src[j] = listOfFiles[i].getName();
						j++;
					}
			}
                }
		for(int i=0;i<listOfFiles.length;i++){
			System.out.println(src[i]);	
		}
                JList<String> list = new JList<String>(listModel);
//		src = (String[]) list.toArray(new String[10]);
                list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                list.setLayoutOrientation(JList.VERTICAL);
                list.setVisibleRowCount(5);
                list.addListSelectionListener(new ListSelectionListener(){
		public void valueChanged(ListSelectionEvent e){
			source = (JList) e.getSource();
			//source.setSelectionMode(1);
                	str = source.getSelectedValue().toString();
			textField = new JTextField(str);
                //System.out.println(str);

		}
});
                JScrollPane ScrollPane = new JScrollPane(list);
                pan.add(ScrollPane);
//	n = source.getSelectedIndex();
		
		next.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
		                //stopBtn.addActionListener(new ActionListener(){
                                //	public void actionPerformed(ActionEvent e){
                                //	stopPlayback = true;
                                //	}
                                //}
                                //);
				//setActionCommand(stopBtn);
				playBtn.setEnabled(true);
				stopPlayback = true;
		//				int n = source.getSelectedIndex();
						n++;			
						System.out.println(n);
						System.out.println(str);
						str = src[n+1];
				//		n++;
						System.out.println(str);
						//stopPlayback = false;
                        			textField = new JTextField(str);
						//stopPlayback = false;
			}			
		});


		playBtn.addActionListener(
				new ActionListener(){
				public void actionPerformed(
						ActionEvent e){
				stopBtn.setEnabled(true);
				n = source.getSelectedIndex();
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
		pan1.add(next);
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
