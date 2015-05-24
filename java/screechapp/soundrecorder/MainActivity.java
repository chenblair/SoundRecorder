package screechapp.soundrecorder;

import java.io.IOException;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.*;
import android.app.*;
import android.content.*;

public class MainActivity extends Activity {

    private MediaRecorder myRecorder;
    private MediaPlayer myPlayer;
    private MediaPlayer addBeat;
    private String outputFile = null;
    private Button startBtn;
    private Button stopBtn;
    private Button playBtn;
    private Button stopPlayBtn;
    private Button beat;
    private TextView text;
    private EditText mEdit;
    private boolean isBongo=false;
    private boolean isSet=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView) findViewById(R.id.text1);
        // store it to sd card
        //outputFile="/mnt/sdcard/Recording.3gpp";
        outputFile = Environment.getExternalStorageDirectory().
                getAbsolutePath() + "/Recording.3gpp";

        myRecorder = new MediaRecorder();
        myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myRecorder.setOutputFile(outputFile);


        startBtn = (Button)findViewById(R.id.start);
        mEdit   = (EditText)findViewById(R.id.fileName);
        startBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setOutputFile(mEdit.getText().toString());
                start(v);
            }
        });

        stopBtn = (Button)findViewById(R.id.stop);
        stopBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                stop(v);
            }
        });

        playBtn = (Button)findViewById(R.id.play);
        playBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                play(v);
            }
        });

        stopPlayBtn = (Button)findViewById(R.id.stopPlay);
        stopPlayBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                stopPlay(v);
            }
        });
        beat = (Button)findViewById(R.id.beat);
        beat.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                beatAdd(v);
            }
        });
    }

    public void start(View view){
        try {
            myRecorder.prepare();
            myRecorder.start();
        } catch (IllegalStateException e) {
            // start:it is called before prepare()
            // prepare: it is called after start() or before setOutputFormat()
            e.printStackTrace();
        } catch (IOException e) {
            // prepare() fails
            e.printStackTrace();
        }

        text.setText("Recording Point: Recording");
        startBtn.setEnabled(false);
        stopBtn.setEnabled(true);

        Toast.makeText(getApplicationContext(), "Start recording...",
                Toast.LENGTH_SHORT).show();
    }

    public void stop(View view){
        try {
            myRecorder.stop();
            myRecorder.release();
            myRecorder  = null;
            //alert();

            stopBtn.setEnabled(false);
            playBtn.setEnabled(true);
            text.setText("Recording Point: Stop recording");

            Toast.makeText(getApplicationContext(), "Stop recording...",
                    Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException e) {
            //  it is called before start()
            e.printStackTrace();
        } catch (RuntimeException e) {
            // no valid audio/video data has been received
            e.printStackTrace();
        }
    }

    public void play(View view) {
        try{
            setOutputFile(mEdit.getText().toString());
            myPlayer = new MediaPlayer();
            myPlayer.setLooping(true);
            myPlayer.setDataSource(outputFile);
            myPlayer.prepare();
            myPlayer.start();


            playBtn.setEnabled(false);
            stopPlayBtn.setEnabled(true);
            text.setText("Recording Point: Playing");

            Toast.makeText(getApplicationContext(), "Start play the recording...",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void stopPlay(View view) {
        try {
                myPlayer.setLooping(false);
                myPlayer.stop();
                myPlayer.release();
                myPlayer = null;
                playBtn.setEnabled(true);
                stopPlayBtn.setEnabled(false);
                text.setText("Recording Point: Stop playing");

                Toast.makeText(getApplicationContext(), "Stop playing the recording...",
                        Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void beatAdd(View view)
    {
        try {
            if (!isBongo) {
                addBeat = MediaPlayer.create(this, R.raw.app);
                addBeat.setLooping(true);
                addBeat.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer arg0) {
                        addBeat.start();

                    }
                });
                isBongo=true;
            } else {
                addBeat.stop();
                addBeat.release();
                addBeat  = null;
                isBongo=false;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void alert()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Title");
        alert.setMessage("Message");

// Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                setOutputFile(value);
                // Do something with value!
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }
    public void setOutputFile(String o)
    {
        if (!isSet) {
            if (o != null)
                outputFile = Environment.getExternalStorageDirectory().
                        getAbsolutePath() + "/" + o + ".3gpp";
            else
                outputFile = Environment.getExternalStorageDirectory().
                        getAbsolutePath() + "/recording.3gpp";
            myRecorder.setOutputFile(outputFile);
            isSet = true;
        }
    }


}