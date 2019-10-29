using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class BtnCtrl : MonoBehaviour
{
	public GameObject[] Buttons_up;
	public GameObject[] Buttons_down;

    // Start is called before the first frame update
    void Start()
    {
        AllBtnDisable();
    }

    // Update is called once per frame	
    void Update()
    {
        
    }

    private void AllBtnDisable()
    {
    	Buttons_up = GameObject.FindGameObjectsWithTag("upbtn");
    	Buttons_down = GameObject.FindGameObjectsWithTag("downbtn");
    	foreach ( GameObject button in Buttons_up )
    	{
    		button.SetActive(false);
    	}
    	foreach ( GameObject button in Buttons_down )
    	{
    		button.SetActive(false);
    	}
    }

    public void ActiveSelectedBtn(GameObject selected)
    {
    	try{
			selected.transform.Find("Up_btn").gameObject.SetActive(true);
			selected.transform.Find("Dn_btn").gameObject.SetActive(true);
    	}
    	catch (NullReferenceException ex){

    	}
    	
    }
}
