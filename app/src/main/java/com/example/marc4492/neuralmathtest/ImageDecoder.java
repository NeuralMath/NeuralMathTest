package com.example.marc4492.neuralmathtest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Class qui contient le réseau de neurones et qui obtient l'équation en string
 *
 * @author Marc4492
 * 10 février 2017
 */

public class ImageDecoder {
    private Activity context;
    private ArrayList<Bitmap> listChar;
    private NeuralNetwork network;
    private String[] charList;

    private int squaredPixNumber;

    /**
     * Contructeur qui initialise le réseau
     *
     * @param c                 Contexte de l'App
     * @param input             Nombre de neurones d'input dans le réseau
     * @param hidden            Nombre de neurones de hidden dans le réseau
     * @param output            Nombre de neurones d'output dans le réseau
     * @param training          Training rate du reseau
     * @param fileWIH           Path du ficher weight entre input et hidden
     * @param fileWHO           Path du ficher weight entre hidden et output
     * @param charListing       List des char avec leur index dans le réseau
     * @throws Exception        S'il y a des problèmes de fichier, ...
     */
    public ImageDecoder(Activity c, final int input, final int hidden, final int output, final double training, final String fileWIH, final String fileWHO, String[] charListing) throws Exception
    {
        context = c;
        listChar = new ArrayList<>();

        squaredPixNumber = (int) Math.sqrt(input);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    network = new NeuralNetwork(input, hidden, output, training, fileWIH, fileWHO);
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                catch (IOException ex)
                {
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Erreur de lecture de fichier", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        t.start();
        charList = charListing;
    }

    /**
     * Obtient l'équation en string
     *
     * @param btm           L'image à décoder
     * @return              l'équation en String
     * @throws Exception    S'il y a des problème avec l'image
     */
    public String findSting(Bitmap btm) throws Exception
    {
        listChar.clear();
        String line = "";

        //Split toutes les chars
        splitChar(btm);

        //Add le char dans l'eq
        for(int i = 0; i < listChar.size(); i++) {
            int index = network.getAnwser(getIOPixels(listChar.get(i)));
            line += charList[index];
        }

        return line;
    }

    /**
     * Split les différents caractère de l'image
     *
     * @param btm               L'image à analyser
     * @throws Exception        S'il y a des problèmes
     */
    private void splitChar(Bitmap btm) throws Exception
    {
        ArrayList<Integer> listWhite = new ArrayList<>();
        int pixel;
        boolean whiteLine;

        //Check chaque colonne pour voir si elle est blanche : check chaque couleurs pour les val hex
        for(int i = 0; i < btm.getWidth(); i++) {
            whiteLine = true;
            for (int j = 0; j < btm.getHeight(); j++) {
                pixel = btm.getPixel(i, j);
                if(Color.red(pixel) <= 0x0C && Color.green(pixel) <= 0x0C && Color.blue(pixel) <= 0x0C)
                {
                    whiteLine = false;
                    break ;
                }
            }
            if(whiteLine)
                listWhite.add(i);
        }
        //Splitter les char
        if(listWhite.size() > 1 && listWhite.size() < btm.getWidth()) {
            //Si la premiere ligne est noir
            if(listWhite.get(0) != 0)
                listChar.add(resize(crop(btm, 0, 0, listWhite.get(0), btm.getHeight())));

            //Les caractère avec une ligne blance de chaque c¸eté sont couper et ajouter à la liste
            for (int i = 1; i < listWhite.size(); i++)
                if (listWhite.get(i) - listWhite.get(i - 1) > 1)
                    listChar.add(resize(crop(btm, listWhite.get(i - 1), 0, listWhite.get(i), btm.getHeight())));

            //Si la derniere ligne est noir
            if(listWhite.get(listWhite.size()-1) != btm.getWidth()-1)
                listChar.add(resize(crop(btm, listWhite.get(listWhite.size()-1), 0, btm.getWidth(), btm.getHeight())));
        }
        else if(listWhite.size() == 0)
            listChar.add(resize(btm));
    }

    /**
     * Rogner l'image selon les paramètre
     *
     * @param bitmap        L'image
     * @return              L'image rogner
     * @throws Exception    S'il y a des problèmes
     */
    private Bitmap crop(Bitmap bitmap, int startX, int startY, int endX, int endY) throws Exception
    {
        return Bitmap.createBitmap(bitmap, startX, startY, endX-startX, endY-startY);
    }

    /**
     * Changer la grandeur de l'image en gardant le ratio
     *
     * @param bitmap        L'image
     * @return              L'image resized
     * @throws Exception    S'il y a des problèmes
     */
    private Bitmap resize(Bitmap bitmap) throws Exception
    {
        return Bitmap.createScaledBitmap(bitmap, squaredPixNumber, squaredPixNumber, false);
    }

    /**
     * Get un array une dimension de la valeur binaire des pixels d'une iimage
     *
     * @param bitmap        L'image
     * @return              Un tableau de int selon les pixels(1 ou 0)
     * @throws Exception    S'il y a des problèmes
     */
    private int[] getIOPixels(Bitmap bitmap) throws Exception
    {
        ArrayList<Integer> pixels = new ArrayList<>();

        //Selon la valeur du pixel, 1 ou 0
        int pixel;
        for(int i = 0; i < bitmap.getWidth(); i++) {
            for (int j = 0; j < bitmap.getHeight(); j++) {
                pixel = bitmap.getPixel(i, j);
                if((Color.red(pixel) + Color.green(pixel) + Color.blue(pixel)) < 383)
                    pixels.add(1);
                else
                    pixels.add(0);
            }
        }

        //Transformation en array de int
        int[] inputValues = new int[bitmap.getWidth()*bitmap.getHeight()];
        for(int i = 0; i < pixels.size(); i++)
            inputValues[i] = pixels.get(i);

        return inputValues;
    }
}