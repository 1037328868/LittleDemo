package com.sagee.zyq.testimagevideo;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBt_get_local_img;
    private Button mBt_get_camera_img;
    private Button mBt_uploading_img;
    private ImageView mIv_head;

    /**
     * 保存临时头像图片的地址
     */
    private static final String IMG_TEMP_FILE = MyAPP.ctx.getFilesDir().getAbsolutePath()+"/ImageHead"+".png";
    /**
     * 相册
     */
    private static final int LOCAL_IMG = 1;
    /**
     * 拍照
     */
    private static final int CAMERA_IMG = 2;
    /**
     * 裁剪
     */
    private static final int PHOTO_CLIP = 3;
    /**
     * 摄像头权限
     */
    private static final int CAMERA_PERMISSON = 4;
    private File mFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        mBt_get_local_img = (Button) findViewById(R.id.bt_get_local_img);
        mBt_get_camera_img = (Button) findViewById(R.id.bt_get_camera_img);
        mBt_uploading_img = (Button) findViewById(R.id.bt_uploading_img);
        mIv_head = (ImageView) findViewById(R.id.iv_head);

        mBt_get_local_img.setOnClickListener(this);
        mBt_get_camera_img.setOnClickListener(this);
        mBt_uploading_img.setOnClickListener(this);

        // 此file用来保存裁剪后的的头像图片
        mFile = new File(IMG_TEMP_FILE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.bt_get_local_img:
                // 点击从本地相册获取图片
                Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                // 如果要限制上传到服务器的图片类型时可以直接写如：image/jpeg 、 image/png等的类型
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(pickIntent, LOCAL_IMG);
                break;

            case R.id.bt_get_camera_img:
                // 点击调用系统拍照获取图片
                // 6.0需要动态申请摄像头权限
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[] { Manifest.permission.CAMERA },CAMERA_PERMISSON);
                    }else {
                        getCameraImg();
                    }
                }else {
                    getCameraImg();
                }

                break;

            case R.id.bt_uploading_img:
                // 点击上传图片至服务器
                // 从本地文件读取
                Bitmap bitmap = BitmapFactory.decodeFile(IMG_TEMP_FILE);
                if (bitmap!=null){
                    mIv_head.setImageBitmap(bitmap);
                }
                break;
        }
    }

    /**
     * 打开摄像头拍照
     */
    private void getCameraImg() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile =  null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_IMG);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case LOCAL_IMG:
                // 从本地相册的中取
                if (data!=null){
                    Uri uri = data.getData();
                    // 对相册取出照片进行裁剪
                    photoClip(uri);
                }
                break;

            case CAMERA_IMG:
                // 裁剪摄像头拍摄的图片
                if (!TextUtils.isEmpty(mCurrentPhotoPath)){
                    photoClip(Uri.fromFile(new File(mCurrentPhotoPath)));
                }
                break;

            case PHOTO_CLIP:
                // 裁剪图片
                if (data!=null){
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        // 获取裁剪好的图片
                        Bitmap photo = extras.getParcelable("data");
                        // 上传完成将照片写入imageview与用户进行交互
//                        mIv_head.setImageBitmap(photo);
                        // 把bitmap保存成文件
                        saveBitmap(photo);
                    }
                }
                break;
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_PERMISSON:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 用户给权限了
                    getCameraImg();
                } else {
                    Toast.makeText(this, "没有权限,无法拍照", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 图片裁剪
     * @param uri
     */
    private void photoClip(Uri uri) {
        // 调用系统中自带的图片剪裁
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_CLIP);
    }

    String mCurrentPhotoPath;

    /**
     * @return 创建一个保存摄像头照片的临时文件
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PNG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * @param bitmap 保存的图片
     */
    private void saveBitmap(Bitmap bitmap){

        FileOutputStream fout = null;
        try {
            // 如果之前保存过
            if(mFile.exists()){
                // 则把之前的文件删除
                mFile.delete();
            }
            mFile.createNewFile();

            fout = new FileOutputStream(mFile);
            // android自带的bitmap保存方法 参数1:图片格式  参数2: 画质1-100,100画质最好  参数3: fileOutputStream
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fout);
            fout.flush();

            Log.e("ImageActivity: ", "保存成功");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fout!=null){
                    fout.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
