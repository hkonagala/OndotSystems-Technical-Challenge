package ondotsystems.com.imagefinder;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.marcoscg.fingerauth.FingerAuth;
import com.marcoscg.fingerauth.FingerAuthDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //TODO
    //6. code structure & readability
    //7. design patterns
    //8. handle orientation changes & edge cases (empty list/network error)

    private FingerAuthDialog fingerAuthDialog;
    boolean hasFingerprintSupport;

    Button authenticateUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authenticateUser = findViewById(R.id.fingerprint_dialog);

        hasFingerprintSupport = FingerAuth.hasFingerprintSupport(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //register click listener
        authenticateUser.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregister click listener
        authenticateUser.setOnClickListener(null);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.fingerprint_dialog) {
            fingerAuthDialog = null;
            //Show dialog only on devices that support fingerprint auth
            if(hasFingerprintSupport) {
                createFingerprintDialog();
            } else {
                //just for the sake of assignment, creating a work around for fingerprint auth
                // but ideally should ask for alternate password/pin
                createContinueDialog();
            }
        }

    }

    private void createFingerprintDialog() {
        fingerAuthDialog = new FingerAuthDialog(this)
                .setTitle(getString(R.string.signin))
                .setCancelable(false)
                .setMaxFailedCount(3)
                .setNegativeButton(getResources().getString(R.string.cancel), null)
                .setOnFingerAuthListener(new FingerAuth.OnFingerAuthListener() {
                    @Override
                    public void onSuccess() {
                        openSearchActivity();
                    }

                    @Override
                    public void onFailure() {
                        //do nothing; handled by fingerprint auth lib
                    }

                    @Override
                    public void onError() {
                        //do nothing; handled by fingerprint auth lib
                    }
                });

        fingerAuthDialog.show();
    }



    private void createContinueDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.fingerprint_error))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openSearchActivity();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void openSearchActivity() {
        //go to search screen
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
    }
}
