using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class AgTest : MonoBehaviour
{
    private AndroidGallery ag = null;

    // Start is called before the first frame update
    void Start()
    {
        Debug.Log("AgTest::Start()");
        ag = AndroidGallery.GetInstance("VRActor1");
    }

    // Update is called once per frame
    void Update()
    {
        if (Input.GetMouseButtonDown(0)) {
                string[] imgs = ag.GetRecent100();
                for (int i = 0; i < 10; i++) {
                    Debug.Log(imgs[i]);
                }
                ag.SendImage(0);
                ag.SendImage("/tmp/test.jpg");
                ag.ReceiveImage();
                ag.Release();
        }
    }
}
