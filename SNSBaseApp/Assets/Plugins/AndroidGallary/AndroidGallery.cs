using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class AndroidGallery //: MonoBehaviour
{
    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        
    }

    public static void Trigger()
    {
        AndroidJavaClass instance = AJC.CallStatic<AndroidJavaClass>("getInstance", (Object) Context, "VRActor1");
        instance.Call("sendImage", 1);
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
