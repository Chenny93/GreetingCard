package com.chenny.facedetection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class MainActivity extends Activity {

    private ImageView ivPhoto;
    private EditText etWord;
    private Button btShare;
    private IWXAPI iwxapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK,null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent,100);
            }
        });

        etWord.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/test.ttf"));

        btShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wechatShare();
                btShare.setVisibility(View.VISIBLE);
            }
        });
    }

    private void wechatShare(){
        WXWebpageObject webpage=new WXWebpageObject();
        WXMediaMessage msg=new WXMediaMessage(webpage);
        msg.mediaObject=new WXImageObject(generateSpringCard());

        SendMessageToWX.Req req=new SendMessageToWX.Req();
        req.transaction=String.valueOf(System.currentTimeMillis());
        req.message=msg;
        req.scene=SendMessageToWX.Req.WXSceneTimeline;
        iwxapi.sendReq(req);
    }

    private Bitmap generateSpringCard(){
        btShare.setVisibility(View.INVISIBLE);
        View view=getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==100){
            if(data!=null){
                ivPhoto.setImageURI(data.getData());
            }
        }
    }

    public void setViews(){
        iwxapi= WXAPIFactory.createWXAPI(this,"wx2c556b157cf5424d");
        iwxapi.registerApp("wx2c556b157cf5424d");
        ivPhoto=(ImageView) findViewById(R.id.ivPhoto);
        etWord=(EditText) findViewById(R.id.etWord);
        btShare=(Button)findViewById(R.id.btShare);
    }
}
