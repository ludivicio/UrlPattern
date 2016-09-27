package url.ancode.org.urlpattern;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private ScrollView mScrollView;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mScrollView = (ScrollView) findViewById(R.id.sv);

        Button btn = (Button) findViewById(R.id.btn_confirm);
        result = (TextView) findViewById(R.id.tv_result);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setText("");
                new ExecuteTask(MainActivity.this).execute();
            }
        });

    }


    private static class ExecuteTask extends AsyncTask<Void, SpannableString, Void> {

        private final WeakReference<MainActivity> mActivity;

        public ExecuteTask(MainActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        protected void onProgressUpdate(SpannableString... values) {
            super.onProgressUpdate(values);

            MainActivity activity = mActivity.get();
            if (activity == null) {
                return;
            }

            TextView mResult = activity.result;
            mResult.append(values[0]);
            mResult.append("\n");

            final ScrollView scrollView = activity.mScrollView;

            scrollView.post(new Runnable() {
                public void run() {
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }

        @Override
        protected Void doInBackground(Void... params) {

            MainActivity activity = mActivity.get();
            if (activity == null) {
                return null;
            }

            String[] urls = activity.getResources().getStringArray(R.array.url_collections);
            String[] results = activity.getResources().getStringArray(R.array.url_results);
            int size = urls.length;
            for (int i = 0; i < size; i++) {
                publishProgress(new SpannableString(urls[i]));

//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                SpannableString ss = UrlUtil.retractUrl(urls[i], results[i]);
                if (ss == null) {
                    SpannableString failed = new SpannableString("提取失败");
                    failed.setSpan(new ForegroundColorSpan(Color.parseColor("#DD5044")), 0, failed.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    publishProgress(failed);
                } else {
                    publishProgress(ss);
                }

                publishProgress(new SpannableString("========================="));
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            MainActivity activity = mActivity.get();
            if (activity == null) {
                return;
            }

            Toast.makeText(activity, "提取完成", Toast.LENGTH_SHORT).show();
        }
    }

}
