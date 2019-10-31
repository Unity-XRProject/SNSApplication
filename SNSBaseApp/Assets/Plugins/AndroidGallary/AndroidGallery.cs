using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class AndroidGallery
{
    private static AndroidGallery instance = null;
    private AndroidJavaObject ag = null;

    protected AndroidGallery(string actorId, int capacity) {
        ag = AJC.CallStatic<AndroidJavaObject>("getInstance", Context, actorId, capacity);
    }

    public static AndroidGallery GetInstance(string actorId, int capacity) {
        if (instance == null) {
            instance = new AndroidGallery(actorId, capacity);
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

    public string[] GetImageList() {
        return ag.Call<string[]>("getImageList");
    }

/*
	public static Texture2D LoadImageAtPath( string imagePath, int maxSize = -1, bool markTextureNonReadable = true,
		bool generateMipmaps = true, bool linearColorSpace = false )
	{
		if( string.IsNullOrEmpty( imagePath ) )
			throw new ArgumentException( "Parameter 'imagePath' is null or empty!" );

		if( !File.Exists( imagePath ) )
			throw new FileNotFoundException( "File not found at " + imagePath );

		if( maxSize <= 0 )
			maxSize = SystemInfo.maxTextureSize;

		string loadPath = imagePath;

		String extension = Path.GetExtension( imagePath ).ToLowerInvariant();
		TextureFormat format = ( extension == ".jpg" || extension == ".jpeg" ) ? TextureFormat.RGB24 : TextureFormat.RGBA32;

		Texture2D result = new Texture2D( 2, 2, format, generateMipmaps, linearColorSpace );

		try
		{
			if( !result.LoadImage( File.ReadAllBytes( loadPath ), markTextureNonReadable ) )
			{
				Object.DestroyImmediate( result );
				return null;
			}
		}
		catch( Exception e )
		{
			Debug.LogException( e );

			Object.DestroyImmediate( result );
			return null;
		}
		finally
		{
			if( loadPath != imagePath )
			{
				try
				{
					File.Delete( loadPath );
				}
				catch { }
			}
		}

		return result;
	}

	public static ImageProperties GetImageProperties( string imagePath )
	{
		if( !File.Exists( imagePath ) )
			throw new FileNotFoundException( "File not found at " + imagePath );

		string value = AJC.CallStatic<string>( "GetImageProperties", Context, imagePath );

		int width = 0, height = 0;
		string mimeType = null;
		ImageOrientation orientation = ImageOrientation.Unknown;
		if( !string.IsNullOrEmpty( value ) )
		{
			string[] properties = value.Split( '>' );
			if( properties != null && properties.Length >= 4 )
			{
				if( !int.TryParse( properties[0].Trim(), out width ) )
					width = 0;
				if( !int.TryParse( properties[1].Trim(), out height ) )
					height = 0;

				mimeType = properties[2].Trim();
				if( mimeType.Length == 0 )
				{
					String extension = Path.GetExtension( imagePath ).ToLowerInvariant();
					if( extension == ".png" )
						mimeType = "image/png";
					else if( extension == ".jpg" || extension == ".jpeg" )
						mimeType = "image/jpeg";
					else if( extension == ".gif" )
						mimeType = "image/gif";
					else if( extension == ".bmp" )
						mimeType = "image/bmp";
					else
						mimeType = null;
				}

				int orientationInt;
				if( int.TryParse( properties[3].Trim(), out orientationInt ) )
					orientation = (ImageOrientation) orientationInt;
			}
		}

		return new ImageProperties( width, height, mimeType, orientation );
	}

	public enum ImageOrientation { Unknown = -1, Normal = 0, Rotate90 = 1, Rotate180 = 2, Rotate270 = 3, FlipHorizontal = 4, Transpose = 5, FlipVertical = 6, Transverse = 7 };
	public struct ImageProperties
	{
		public readonly int width;
		public readonly int height;
		public readonly string mimeType;
		public readonly ImageOrientation orientation;

		public ImageProperties( int width, int height, string mimeType, ImageOrientation orientation )
		{
			this.width = width;
			this.height = height;
			this.mimeType = mimeType;
			this.orientation = orientation;
		}
	}
*/

    private static AndroidJavaClass m_ajc = null;
    private static AndroidJavaClass AJC
    {
        get
        {
            if( m_ajc == null )
                m_ajc = new AndroidJavaClass( "com.sds.xr.sns.lib.AndroidGallery" );
            Debug.Log("ajc = " + m_ajc);
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

            Debug.Log("Context = " + m_context);
            return m_context;
        }
    }
}
