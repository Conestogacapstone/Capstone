

package com.example.android.capstone_project;
 
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Preferences extends PreferenceActivity
{
  @Override
  protected void onCreate(Bundle bundle)
  {
    super.onCreate(bundle);
    addPreferencesFromResource(R.xml.preferences);
  }
}

