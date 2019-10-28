using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class AndroidGallery
{
    private static AndroidGallery instance = null;
    private AndroidJavaObject ag = null;

    protected AndroidGallery(string actorId) {
        ag = AJC.CallStatic<AndroidJavaObject>("getInstance", Context, actorId);
    }

    public static AndroidGallery GetInstance(string actorId) {
        if (instance == null) {
            instance = new AndroidGallery(actorId);
        }
        return instance;
    }

    public void SendImage(int idx) {
        Debug.Log("SendImage: idx = " + idx);
        ag.Call("sendImage", idx);
    }
    public void SendImage(string path) {
        Debug.Log("SendImage: path = " + path);
        ag.Call("sendImage", path);
    }

    public void ReceiveImage() {
        Debug.Log("ReceiveImage");
        ag.Call("receiveImage");
    }

    public void Release() {
        Debug.Log("Release");
        ag.Call("release");
    }

    public string[] GetRecent100() {
        return ag.Call<string[]>("getRecent100");
    }

    private static AndroidJavaClass m_ajc = null;
    private static AndroidJavaClass AJC
    {
        get
        {
            if( m_ajc == null )
                m_ajc = new AndroidJavaClass( "com.sds.xr.sns.lib.AndroidGallery" );

            return m_ajc;
        }
    }

    private static AndroidJavaObject m_context = null;
    private static AndroidJavaObject Context
    {
        get
        {
            if( m_context == null )
            {
                using( AndroidJavaObject unityClass = new AndroidJavaClass( "com.unity3d.player.UnityPlayer" ) )
                {
                    m_context = unityClass.GetStatic<AndroidJavaObject>( "currentActivity" );
                }
            }

            return m_context;
        }
    }
}
