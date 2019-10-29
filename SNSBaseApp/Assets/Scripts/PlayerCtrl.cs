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
    int speed = 100;

    public float RotationSpeed = 0.0f;

    // Start is called before the first frame update
    void Start()
    {
        ScreenCenter = new Vector3(Camera.main.pixelWidth / 2, Camera.main.pixelHeight / 2);   
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
        	
	            if (hit.collider.CompareTag("leftbtn"))
	            {
	                GageTimer += 1.0f / 2.0f * Time.deltaTime;

	                if (GageTimer >= 1)
	                {
	                    //Rotbox.transform.rotation += Quaternion.Euler(0, -90.0f, 0);
	                    //Rotbox.transform.Rotate(new Vector3(0, -90.0f, 0),Space.World);
	                    //rotbox.transform.rotation = Quaternion.Slerp(rotbox.transform.rotation, RotationQ, RotationSpeed * Time.deltaTime);
	                    GageTimer = 0;
	                }
	            }
	            else if(hit.collider.CompareTag("rightbtn"))
	            {
	                GageTimer += 1.0f / 2.0f * Time.deltaTime;

	                if (GageTimer >= 1)
	                {
	                    //Rotbox.transform.rotation += Quaternion.Euler(0, 90.0f, 0);
	                    //Rotbox.transform.Rotate(new Vector3(0, 90.0f, 0),Space.World);
	                    GageTimer = 0;
	                }
	            }
	            else if (hit.collider.CompareTag("upbtn"))
	            {
	                GageTimer += 1.0f / 2.0f * Time.deltaTime;

	                if (GageTimer >= 1)
	                {
	                    //Rotbox.transform.rotation = Quaternion.Euler(-90.0f, 0, 0);
	                    //Rotbox.transform.Rotate(new Vector3(-90.0f, 0, 0),Space.World);
	                    GageTimer = 0;
	                }
	            }
	            else if (hit.collider.CompareTag("downbtn"))
	            {
	                GageTimer += 1.0f / 2.0f * Time.deltaTime;

	                if (GageTimer >= 1)
	                {
	                    //Rotbox.transform.rotation = Quaternion.Euler(90.0f, 0, 0);
	                    //Rotbox.transform.Rotate(new Vector3(90.0f, 0,0),Space.World);
	                    GageTimer = 0;
	                }
	            }
	            else if (hit.collider.CompareTag("Quad"))
	            {
	                GageTimer += 1.0f / 2.0f * Time.deltaTime;

	                if (GageTimer >= 1)
	                {
	                	GameObject quad = hit.collider.gameObject;
	                	BtnCtrl.ActiveSelectedBtn(quad);
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
        }
        }       
        catch (NullReferenceException ex) {
        	//Debug.Log("Error in PlayerCtrl" + ex);
        }
    }
	
}