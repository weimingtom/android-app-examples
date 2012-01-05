#ifndef __CONTROLLAYER_H__
#define __CONTROLLAYER_H__

#include "MTGame.h"

using namespace cocos2d;

class ControlLayer: public CCLayer
{
public:
	ControlLayer(void);
	~ControlLayer(void);

	void reset();

	LAYER_NODE_FUNC(ControlLayer);
protected:
	CCTexture2D* texture_default;
	CCTexture2D* texture_up;
	CCTexture2D* texture_right;
	CCTexture2D* texture_down;
	CCTexture2D* texture_left;
	CCSprite *hudSprite;
	int controlDirection;

	virtual bool init();
	virtual void onExit();
	virtual void registerWithTouchDispatcher();

	void updateControlInput(ccTime dt);

	bool ccTouchBegan(CCTouch* touch, CCEvent* event);
	void ccTouchEnded(CCTouch* touch, CCEvent* event);
	void ccTouchCancelled(CCTouch* touch, CCEvent* event);
	void ccTouchMoved(CCTouch* touch, CCEvent* event);
	bool handleTouchEvent(CCTouch* touch);
};

#endif
