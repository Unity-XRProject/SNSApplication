using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class BtnCtrl : MonoBehaviour
{
	public GameObject[] Buttons_send;

    // Start is called before the first frame update
    void Start()
    {
        //AllBtnDisable();
    }

    // Update is called once per frame	
    void Update()
    {
        
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
    	Debug.Log("Selected Quad = "+selected);
    	//Debug.Log("Selected Quad's Children = "+selected.transform.GetComponentsInChildren<GameObject>());
    	try{
			selected.transform.Find("SendBtn").gameObject.SetActive(true);
    	}
    	catch (NullReferenceException ex){

    	}
    	
    }
}
