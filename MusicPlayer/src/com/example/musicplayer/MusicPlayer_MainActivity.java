package com.example.musicplayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

	 public class MusicPlayer_MainActivity extends Activity {
	    
	   Song song = null;
	   List<Integer> list =  new ArrayList<Integer>();
	   MediaPlayer mediaPlayer , mediaPlayerNext;
	   ImageButton play, back, next, random;
	   TextView tgbd, tgkt, tenbh, tencs;	
	   SeekBar sb;
	   private static int loadSeekBar = 0;// load thanh nhạc
	   private double thoigianbatdau = 0; // thời gian bắt đầu nhạc đồng thời là thời gian hiện tại
	   private double thoigianketthuc = 0;// thời gian kết thúc nhạc
	   private int b = 0, a = 0, count = 0, q = 0, i = 0, m =0, e = 0, n = 0;;// a gán nhạc chế độ ngẫu nhiên, b chạy bình thường đồng thời check hết nhạc, q reset nút ngẫu nhiên, i check hết nhạc, m, e, n gán nhạc khi nhất nút và đổi chế độ
	   private boolean kt = true; // kiểm tra đổi chế độ
	   private Handler myHanler = new Handler();// trong một khoảng thời gian nào đó phương thức này sẽ gọi hàm runnable   
	  
	   @Override
	   protected void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.activity_music_player__main);
	       // Ánh xạ
	       final ArrayList<Song> arr = new ArrayList<Song>();
	       play = (ImageButton) findViewById(R.id.imageButton2);
	       back = (ImageButton) findViewById(R.id.imageButton3);
	       next = (ImageButton) findViewById(R.id.imageButton4);	
	       random = (ImageButton) findViewById(R.id.imageButton1);	       
	       
	       tgbd = (TextView) findViewById(R.id.tvbd);
	       tgkt = (TextView) findViewById(R.id.tvkt);
	       tenbh = (TextView) findViewById(R.id.textViewTenBH);
	       tencs = (TextView) findViewById(R.id.textViewTenCS);	       
	       
	       sb = (SeekBar) findViewById(R.id.seekBar1);   
	       // Animation của imagebutton
	       final RotateAnimation rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(this,R.anim.rotate);
	       //lấy nhạc trong file xml
	       try{
	           DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	           DocumentBuilder db= dbf.newDocumentBuilder();
	           Document doc = db.parse(getAssets().open("listsong.xml"));
	            
	           //Lấy về node gốc của mỗi bài hát
	           NodeList nodeList = doc.getElementsByTagName("song");
	           for(int i=0; i<nodeList.getLength(); i++){
	               Node node = nodeList.item(i);
	               if(node.getNodeType() == Node.ELEMENT_NODE){
	                   song = new Song();
	                   Element elm = (Element)node;
	                   //Id
	                   NodeList idList = elm.getElementsByTagName("id");
	                   Element idElement = (Element)idList.item(0);
	                   song.setId(idElement.getTextContent().trim());
	                    
	                   //title
	                   NodeList titleList = elm.getElementsByTagName("title");
	                   Element titleElement = (Element)titleList.item(0);
	                   song.setTitle(titleElement.getTextContent().trim());
	                    
	                   //singer
	                   NodeList singerList = elm.getElementsByTagName("singer");
	                   Element singerElement = (Element)singerList.item(0);
	                   song.setSinger(singerElement.getTextContent().trim());
	                    
	                   //duration
	                   NodeList durationList = elm.getElementsByTagName("duration");
	                   Element durationElement = (Element)durationList.item(0);
	                   song.setDuration(durationElement.getTextContent().trim());
	                    
	                   //icon
	                   NodeList iconList = elm.getElementsByTagName("icon");
	                   Element iconElement = (Element)iconList.item(0);
	                   song.setIcon(iconElement.getTextContent().trim());
	                   arr.add(song);
	               }
	           }
	       }catch(Exception e){
	            
	       }
	       // gọi listview
	       ListView lv = (ListView)findViewById(R.id.list);
	       MyArrayAdapter mayArr = new MyArrayAdapter(this, R.layout.list_row, arr);
	       lv.setAdapter(mayArr);
	       count = lv.getAdapter().getCount();// đếm số lượng phần tử adapter
	       // xử lý sự kiện khi click vào một dòng listview nào đó sẽ bắt đầu nhạc
	       lv.setOnItemClickListener(
					new AdapterView.OnItemClickListener() {
						public void onItemClick(AdapterView<?> arg0,
								View arg1, 
								int arg2,
								long arg3) {
							b = arg2;	// b = vị trí vừa click						
							tenbh.setText(getTen(arg2));	//gọi tên bài hát
							tencs.setText(getTenCS(arg2));	// gọi tên ca sĩ
							musicPlay(arg2);	// chạy nhạc
							play.startAnimation(rotateAnimation);	// chạy hiệu ứng cho nút khi click
						}
					});
	      // hàm pause đồng thời play nhạc
	       play.setOnClickListener(new OnClickListener() {
				
	   				@Override
	   				public void onClick(View v) {
	   					// TODO Auto-generated method stub	
	   					try{	//xử lý khi chưa chọn bài hát
	   						if(mediaPlayer.isPlaying()){	// nếu nhạc đang chơi 
	   							mediaPlayer.pause();   // dừng nhạc
	   							play.setImageResource(R.drawable.play);	// đổi thành nút play
	   							play.clearAnimation();	// tắt hiệu ứng nút
	   						}
	   						else {
					   			int length=mediaPlayer.getCurrentPosition();	// thời gian hiện tại của nhạc
					   		  	mediaPlayer.seekTo(length);		//	cho phép bắt đầu nhạc ở 1 thời gian truyền vào 			   		  		
					   		  	mediaPlayer.start();	// bắt đầu nhạc
					   		   	play.setImageResource(R.drawable.pause);	// chuyển thành nút pause
					   		   	play.startAnimation(rotateAnimation);	// bắt đầu hiệu ứng nút
	   						}
				   		}	   					
	   					catch(Exception e){	   						
	   						Toast.makeText(getApplicationContext(), "Vui lòng chọn bài hát...!",Toast.LENGTH_SHORT).show();
	   					}
	   				}
	   		});	       
	      // lui nhạc 	      
	       back.setOnClickListener(new OnClickListener() {
				
  				@Override
  				public void onClick(View v) {
  					if(kt == false){	// nếu chệ độ ngẫu nhiên
  						if(q>=count-1){	// nếu q > mảng lưu giá trị ngẫu nhiên không trùng lặp					
  							q=0;	// cho q = 0 để ngẫu nhiên lại từ đầu
  							list.clear();	// xóa phần tử trong list
  						}  						
					 	else
					 		if(mediaPlayer.isPlaying()){	// tương tự hàm play, pause, đồng thời các hàm khác cũng tương tự
					 			mediaPlayer.stop();
					 			n = check();
					 			a = n;
					 			play.startAnimation(rotateAnimation);
					 			musicPlay(n);					 			
					 			tenbh.setText(getTen(n));
					 			tencs.setText(getTenCS(n));
					 		}
					 		else{
					 			n = check();
					 			a = n;
					 			musicPlay(n);
					 			play.startAnimation(rotateAnimation);
					 			tenbh.setText(getTen(n));
	       						tencs.setText(getTenCS(n));
					 		}					 		
  						}  	  					
  					else{
  						if(b<=0){
  					 		Toast.makeText(getApplicationContext(), "Hết nhạc, không thể back...!",Toast.LENGTH_SHORT).show();	  					
  					 	}
					 	else
					 		if(mediaPlayer.isPlaying()){
					 			mediaPlayer.stop();
					 			b=b-1;
					 			play.startAnimation(rotateAnimation);
					 			musicPlay(b);					 			
					 			tenbh.setText(getTen(b));
					 			tencs.setText(getTenCS(b));
					 		}
					 		else{
					 			b=b-1;
					 			musicPlay(b);
					 			play.startAnimation(rotateAnimation);
					 			tenbh.setText(getTen(b));
	       						tencs.setText(getTenCS(b));
					 		}					 		
  						}
  					}  	  						  					      
  			});	       
	       // tiến nhạc
	       next.setOnClickListener(new OnClickListener() {
				
  				@Override
  				public void onClick(View v) {
  					// TODO Auto-generated method stub  
  					if(kt == false){
  						if(q>=count-1){  							
  							q=0;
  							list.clear();
  						}  						
					 	else
					 		if(mediaPlayer.isPlaying()){
					 			mediaPlayer.stop();
					 			e = check();
					 			a = e;
					 			play.startAnimation(rotateAnimation);
					 			musicPlay(e);					 			
					 			tenbh.setText(getTen(e));
					 			tencs.setText(getTenCS(e));
					 		}
					 		else{
					 			e = check();
					 			a = e;
					 			musicPlay(e);
					 			play.startAnimation(rotateAnimation);
					 			tenbh.setText(getTen(e));
	       						tencs.setText(getTenCS(e));
					 		}					 		
  						}  	  					
  					else{
  						if(b>=count - 1){
  					 		Toast.makeText(getApplicationContext(), "Hết nhạc, không thể next...!",Toast.LENGTH_SHORT).show();	  					
  					 	}
					 	else
					 		if(mediaPlayer.isPlaying()){
					 			mediaPlayer.stop();
					 			b=b+1;
					 			play.startAnimation(rotateAnimation);
					 			musicPlay(b);					 			
					 			tenbh.setText(getTen(b));
					 			tencs.setText(getTenCS(b));
					 		}
					 		else{
					 			b=b+1;
					 			musicPlay(b);
					 			play.startAnimation(rotateAnimation);
					 			tenbh.setText(getTen(b));
	       						tencs.setText(getTenCS(b));
					 		}					 		
  						}
  					}  	  						  					      
  			});	       	    
	       // tua nhạc
	       sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				mediaPlayer.start();	//	nhả chuột thì bắt đầu nhạc
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				mediaPlayer.pause();	//	click chuột vào seekbar nhạc dừng
			}
			
			@Override
			public void onProgressChanged(SeekBar sb, int progress, boolean b) {
				// TODO Auto-generated method stub
					if(mediaPlayer != null && b){
						mediaPlayer.seekTo(progress);	// lấy thời gian bắt đầu nhạc
					}
			}
		});	       
	       // ngẫu nhiên nhạc
	       random.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub	
				if(q>=count-1){
					q=0;
					list.clear();
				}							
				if(kt == true){						
					kt = false;
					a=check();						
					musicPlay(a);
					play.startAnimation(rotateAnimation);
					tenbh.setText(getTen(a));
					tencs.setText(getTenCS(a));
					random.setImageResource(R.drawable.random_2);
				}						
				else{
					kt = true;
					b = a;
					musicPlay(a);						
					play.startAnimation(rotateAnimation);
					tenbh.setText(getTen(a));
					tencs.setText(getTenCS(a));
					random.setImageResource(R.drawable.random_1);
				}					
			}			
		});
	   }
	   // hàm chạy nhạc
       public void musicPlay(int arg2){

           int id=0;           
           if(arg2==0){
               id=R.raw.someonelikeyou;
           }
           if(arg2==1){
               id=R.raw.strangerinmoscow;
           }
           if(arg2==2){
               id=R.raw.timlaibautroi;
           }
           if(arg2==3){
               id=R.raw.chongaymuatan;
           }
           if(arg2==4){
               id=R.raw.hoccachdimotminh;
           }
           if(arg2==5){
               id=R.raw.chitoi;
           }
           if(arg2==6){
               id=R.raw.somebodysme;
           }
           if(arg2==7){
        	   id=R.raw.lactroi;
           }
           if(mediaPlayer==null){
        	   mediaPlayer=MediaPlayer.create(this, id);
        	   mediaPlayer.start();
           }
           mediaPlayer.pause();
           mediaPlayer.reset();
           mediaPlayer = MediaPlayer.create(this, id);
           try {
			mediaPlayer.prepare();
           } catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
           } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
           }
           mediaPlayer.start();
           play.setImageResource(R.drawable.pause);           
           if(kt == true){
        	   mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer arg0) {
					// TODO Auto-generated method stub
					if(b>=count - 1){
						play.setImageResource(R.drawable.play);
						play.clearAnimation();
				 		Toast.makeText(getApplicationContext(), "Hết nhạc, không thể next...!",Toast.LENGTH_SHORT).show();	  					
				 	}
					else{
						b++;
						musicPlay(b);
						tenbh.setText(getTen(b));
						tencs.setText(getTenCS(b));
					}	
				}
			});
           }
           else{
        	   mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer arg0) {
					// TODO Auto-generated method stub
					if(i>=count - 1){
						play.setImageResource(R.drawable.play);
						play.clearAnimation();
					 	Toast.makeText(getApplicationContext(), "Hết nhạc, không thể next...!",Toast.LENGTH_SHORT).show();	  					
					 }
					else{
						i++;					
						m = check();
						a = m;
						musicPlay(m);
						tenbh.setText(getTen(m));						
						tencs.setText(getTenCS(m));
					}						
				}
			});
           }
           thoigianbatdau = mediaPlayer.getCurrentPosition();
           thoigianketthuc = mediaPlayer.getDuration();
           if(loadSeekBar==0){
        	   sb.setMax((int) thoigianketthuc);        	   
           }
           sb.setProgress((int) thoigianbatdau);
           convert_times();
           myHanler.postDelayed(update_time, 100);          
       }
       // hàm chuyển mili giây sang phút và giây
       public void convert_times (){
    	   // chuyển thời gian bắt đầu
    	   long phutBD = (long) (((thoigianbatdau / 1000) / 60) % 60);	
    	   long giayBD = (long) ((thoigianbatdau / 1000) % 60);
    	   tgbd.setText(String.format("%d:%d",phutBD,giayBD));
    	   
    	   // chuyển thời gian kết thúc
    	   long phutKT = (long) (((thoigianketthuc / 1000) / 60) % 60);	
    	   long giayKT = (long) ((thoigianketthuc / 1000) % 60);
    	   tgkt.setText(String.format("%d:%d",phutKT,giayKT));
       }
       // hàm cập nhập liên tục seekbar theo thời gian nhạc đang chạy
       private Runnable update_time = new Runnable() {
    	   
		@Override
		public void run() {
			// TODO Auto-generated method stub
			thoigianbatdau = mediaPlayer.getCurrentPosition();
			long phutBD = (long) (((thoigianbatdau / 1000) / 60) % 60);	
			long giayBD = (long) ((thoigianbatdau / 1000) % 60);
			if(giayBD < 10){	// nếu giây <10 thì trước giây thêm số 0
				tgbd.setText(String.format("%d:0%d",phutBD,giayBD));
			}
			else{
				tgbd.setText(String.format("%d:%d",phutBD,giayBD));
			} 		

			myHanler.postDelayed(this, 100); // giãn cách thời gian gọi điểm seekbar
			sb.setProgress((int) thoigianbatdau);	// cập nhập seekbar tại thời gian hiện tại
		}
       };
       // hàm lấy tên bài hát trong file xml
       public String getTen (int i){
    	   String a = null;
    	   try{			
    		   DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    		   DocumentBuilder db= dbf.newDocumentBuilder();
    		   Document doc = db.parse(getAssets().open("listsong.xml"));	            
    		   //Lấy về node gốc của mỗi bài hát
		       NodeList nodeList = doc.getElementsByTagName("song");
		       Node node = nodeList.item(i);
		       if(node.getNodeType() == Node.ELEMENT_NODE){
		    	   song = new Song();
		           Element elm = (Element)node;
		           //title
		           a = elm.getElementsByTagName("title").item(0).getTextContent();
		       }	           
		       }
    	   catch(Exception e){
    	   }
    	   return a;
		}	
       // hàm lấy tên ca sĩ trong file xml
       public String getTenCS (int i){
    	   String c = null;
    	   try{			
    		   DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    		   DocumentBuilder db= dbf.newDocumentBuilder();
		       Document doc = db.parse(getAssets().open("listsong.xml"));	            
		       //Lấy về node gốc của mỗi bài hát
		       NodeList nodeList = doc.getElementsByTagName("song");
		       Node node = nodeList.item(i);
		       if(node.getNodeType() == Node.ELEMENT_NODE){
		    	   song = new Song();
		    	   Element elm = (Element)node;
		           //title		                   
		           c = elm.getElementsByTagName("singer").item(0).getTextContent();
		       }	           
    	   }
    	   catch(Exception e){
    	   }
    	   return c;
		}
       // hàm trả về một giá trị ngẫu nhiên
	   public int RANDOM() {
		   int max=count-1;
		   int min=0;
		   int diff=max-min;
		   Random rn = new Random();
		   int i = rn.nextInt(diff+1);
		   i+=min;  
		   return i;
	    }
	   // hàm check ngẫu nhiên không trùng lặp đồng thời trả về giá trị ngẫu nhiên
	   public int check(){ 		   
		   int a = RANDOM(); // ngẫu nhiên
		   while (list.contains(a)){	// nếu có trong list
			   a = RANDOM();	// tiếp tục ngẫu nhiên đến khi nào không trùng với phần tử tồn tại trong list
		   }
		   list.add(a); // chọn được giá trị ngẫu nhiên thêm vào list
		   q++;	// đếm số lần thêm vào list để reset lần click nút ngẫu nhiên và số lượng ngẫu nhiên vừa = số lượng bài hát
		   return a;	// trả vị trí bài hát
	   	}	    
	}	
	