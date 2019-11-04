using System;	
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class PlayerCtrl : MonoBehaviour
{
    public Image CursorGageImage;
    public GameObject Rotbox;
    public GameObject go;
    public Ray ray;
    public RaycastHit hit;
    private Vector3 ScreenCenter;
    private float GageTimer = 0.0f;
    private Quaternion RotationQ;
    private BtnCtrl BtnCtrl;
    public GameObject[] Buttons_send;

    int speed = 100;

    public float RotationSpeed = 0.0f;

    // Start is called before the first frame update
    void Start()
    {
        //ScreenCenter = new Vector3(Camera.main.pixelWidth / 2, Camera.main.pixelHeight / 2);
        AllBtnDisable();
    }

    // Update is called once per frame
    void Update()
    {
    	try {
        ScreenCenter = new Vector3(Camera.main.pixelWidth / 2, Camera.main.pixelHeight / 2);
        ray = Camera.main.ScreenPointToRay(ScreenCenter);
        CursorGageImage.fillAmount = GageTimer;
        if (Physics.Raycast(ray, out hit, 100.0f))
        {
            Debug.Log("Hitted Object = "+hit.collider.tag);
	            if (hit.collider.CompareTag("SendBtn"))
	            {
	                GageTimer += 1.0f / 1.5f * Time.deltaTime;

	                if (GageTimer >= 1)
	                {
	                    //Rotbox.transform.rotation = Quaternion.Euler(-90.0f, 0, 0);
	                    //Rotbox.transform.Rotate(new Vector3(-90.0f, 0, 0),Space.World);
	                    AllBtnDisable();
	                    GageTimer = 0;
	                }
	            }
	            else if (hit.collider.CompareTag("Quad"))
	            {
	                GageTimer += 1.0f / 1.5f * Time.deltaTime;

	                if (GageTimer >= 1)
	                {
	                	GameObject quad = hit.collider.gameObject;
	                	//Debug.Log("Selected Quad = "+quad+"  in Player");
	                	ActiveSelectedBtn(quad);
	                	//Vector3 Gage = ScreenCenter;
	                    //Vector3 offset = (quad.transform.position-Gage);
	                    //float fMove = Time.deltaTime * speed;
	                    //quad.transform.Translate(offset * fMove);
	                    //Rotbox.transform.Rotate(new Vector3(90.0f, 0,0),Space.World);
	                    GageTimer = 0;
	                }
	            }
	            else
	            {
	                GageTimer = 0;
	            }
        }
        else
        {
            GageTimer = 0;
            //AllBtnDisable();
        }
        }       
        catch (NullReferenceException ex) {
        	//Debug.Log("Error in PlayerCtrl" + ex);
        }
    }
    public void AllBtnDisable()
    {
    	Buttons_send = GameObject.FindGameObjectsWithTag("SendBtn");
    	foreach ( GameObject button in Buttons_send )
    	{
    		button.SetActive(false);
    	}
    }
    
    public void ActiveSelectedBtn(GameObject selected)
    {
    	//Debug.Log("Selected Quad = "+selected);
    	//Debug.Log("Selected Quad's Children = "+selected.transform.GetComponentsInChildren<GameObject>());
    	try{
			selected.transform.Find("SendBtn").gameObject.SetActive(true);
    	}
    	catch (NullReferenceException ex){

    	}
    	
    }
}