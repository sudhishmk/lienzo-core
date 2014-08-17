/*
   Copyright (c) 2014 Ahome' Innovation Technologies. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.ait.lienzo.client.core.shape;

import com.ait.lienzo.client.core.Attribute;
import com.ait.lienzo.client.core.Context2D;
import com.ait.lienzo.client.core.image.SpriteLoadedHandler;
import com.ait.lienzo.client.core.shape.json.IFactory;
import com.ait.lienzo.client.core.shape.json.validators.ValidationContext;
import com.ait.lienzo.client.core.shape.json.validators.ValidationException;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.client.core.types.SpriteMap;
import com.ait.lienzo.shared.core.types.ShapeType;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.json.client.JSONObject;

public class Sprite extends Shape<Sprite>
{
    private int                 m_cframe = 0;

    private BoundingBox[]       m_frames = null;

    private ImageElement        m_sprite = null;

    private SpriteLoadedHandler m_loaded = null;

    public Sprite(String url, double rate, SpriteMap smap, String name)
    {
        super(ShapeType.SPRITE);

        setURL(url).setFrameRate(rate).setSpriteMap(smap).setSpriteMapName(name);
    }

    public Sprite(JSONObject node, ValidationContext ctx) throws ValidationException
    {
        super(ShapeType.SPRITE, node, ctx);
    }

    public String getURL()
    {
        return getAttributes().getURL();
    }

    public Sprite setURL(String url)
    {
        getAttributes().setURL(url);

        return this;
    }

    public double getFrameRate()
    {
        return getAttributes().getFrameRate();
    }

    public Sprite setFrameRate(double rate)
    {
        getAttributes().setFrameRate(rate);

        return this;
    }

    public SpriteMap getSpriteMap()
    {
        return getAttributes().getSpriteMap();
    }

    public Sprite setSpriteMap(SpriteMap smap)
    {
        getAttributes().setSpriteMap(smap);

        return this;
    }

    public String getSpriteMapName()
    {
        return getAttributes().getSpriteMapName();
    }

    public Sprite setSpriteMapName(String name)
    {
        getAttributes().setSpriteMapName(name);

        return this;
    }

    public Sprite setAutoPlay(boolean play)
    {
        getAttributes().setAutoPlay(play);

        return this;
    }

    public boolean isAutoPlay()
    {
        return getAttributes().isAutoPlay();
    }

    public Sprite play()
    {
        return this;
    }

    public Sprite pause()
    {
        return this;
    }

    public boolean isPlaying()
    {
        return false;
    }

    @Override
    protected boolean prepare(Context2D context, Attributes attr, double alpha)
    {
        if ((null != m_frames) && (null != m_sprite) && (m_cframe < m_frames.length))
        {
            BoundingBox bbox = m_frames[m_cframe];

            if (null != bbox)
            {
                context.save();

                if (context.isSelection())
                {
                    context.setGlobalAlpha(1);

                    context.setFillColor(getColorKey());

                    context.fillRect(0, 0, bbox.getWidth(), bbox.getHeight());
                }
                else
                {
                    context.setGlobalAlpha(alpha);

                    context.drawImage(m_sprite, bbox.getX(), bbox.getY(), bbox.getWidth(), bbox.getHeight(), 0, 0, bbox.getWidth(), bbox.getHeight());
                }
                context.restore();
            }
        }
        return false;
    }

    public Sprite onLoaded(SpriteLoadedHandler handler)
    {
        m_loaded = handler;

        if (null != m_sprite)
        {
            m_loaded.onSpriteLoaded(this);
        }
        return this;
    }

    @Override
    public IFactory<Sprite> getFactory()
    {
        return new SpriteFactory();
    }

    public static class SpriteFactory extends ShapeFactory<Sprite>
    {
        public SpriteFactory()
        {
            super(ShapeType.SPRITE);

            addAttribute(Attribute.URL, true);

            addAttribute(Attribute.FRAME_RATE, true);

            addAttribute(Attribute.SPRITE_MAP, true);

            addAttribute(Attribute.SPRITE_MAP_NAME, true);

            addAttribute(Attribute.AUTO_PLAY);
        }

        @Override
        public Sprite create(JSONObject node, ValidationContext ctx) throws ValidationException
        {
            return new Sprite(node, ctx);
        }
    }
}
