#include "MTGame.h"

ControlLayer::ControlLayer(void)
{
	sGlobal->controlLayer = this;
	controlDirection = kNormal;
}

ControlLayer::~ControlLayer(void)
{
	CC_SAFE_RELEASE(texture_default)
	CC_SAFE_RELEASE(texture_up)
	CC_SAFE_RELEASE(texture_right)
	CC_SAFE_RELEASE(texture_down)
	CC_SAFE_RELEASE(texture_left)

	this->unscheduleAllSelectors();
}

bool ControlLayer::init()
{
	if ( !CCLayer::init() )
	{
		return false;
	}

	texture_default = CCTextureCache::sharedTextureCache()->addImage("btn_normal.png");
	texture_up = CCTextureCache::sharedTextureCache()->addImage("btn_up.png");
	texture_right = CCTextureCache::sharedTextureCache()->addImage("btn_right.png");
	texture_down = CCTextureCache::sharedTextureCache()->addImage("btn_down.png");
	texture_left = CCTextureCache::sharedTextureCache()->addImage("btn_left.png");

	texture_default->retain();
	texture_up->retain();
	texture_right->retain();
	texture_down->retain();
	texture_left->retain();

	hudSprite = CCSprite::spriteWithTexture(texture_default, CCRectMake(0,0,124,124));
	CCSize size = hudSprite->getContentSize();
	hudSprite->setPosition(ccp(size.width / 2 + 10, size.height / 2 + 14));
	hudSprite->setScale(0.95f);
	hudSprite->setOpacity(180);
	this->addChild(hudSprite);	schedule(schedule_selector(ControlLayer::updateControlInput));
	this->setAnchorPoint(CCPointZero);	this->setPosition(CCPointZero);	this->setIsTouchEnabled(true);
	return true;
}


void ControlLayer::updateControlInput(ccTime dt)
{
	if (controlDirection != kNormal)
	{
		sGlobal->hero->move((HeroDirection)controlDirection);
	}
}

void ControlLayer::registerWithTouchDispatcher()
{
	CCTouchDispatcher::sharedDispatcher()->addTargetedDelegate(this, -3, false);
}

bool ControlLayer::handleTouchEvent(CCTouch* touch) 
{
	CCPoint loc = touch->locationInView( touch->view() );
	loc = CCDirector::sharedDirector()->convertToGL(loc);
	CCPoint hudPosition = hudSprite->getPosition();

	if (loc.x < hudPosition.x - 94 || loc.x > hudPosition.x + 94 || loc.y < hudPosition.y - 94
		|| loc.y > hudPosition.y + 94) {
			hudSprite->setTexture(texture_default);
			return false;
	}

	float x = loc.x - hudPosition.x;
	float y = loc.y - hudPosition.y;

	if (x - y > 0 && x + y > 0) {
		controlDirection = kRight;
		hudSprite->setTexture(texture_right);
	}
	if (x - y > 0 && x + y <= 0) {
		controlDirection = kDown;
		hudSprite->setTexture(texture_down);
	}
	if (x - y < 0 && x + y <= 0) {
		controlDirection = kLeft;
		hudSprite->setTexture(texture_left);
	}
	if (x - y < 0 && x + y > 0) {
		controlDirection = kUp;
		hudSprite->setTexture(texture_up);
	}
	return true;
}

bool ControlLayer::ccTouchBegan(CCTouch* touch, CCEvent* event)
{
	return handleTouchEvent(touch);
}

void ControlLayer::ccTouchMoved(CCTouch* touch, CCEvent* event)
{	handleTouchEvent(touch);
}

void ControlLayer::ccTouchEnded(CCTouch* touch, CCEvent* event)
{
	hudSprite->setTexture(texture_default);
	controlDirection = kNormal;
}

void ControlLayer::ccTouchCancelled(CCTouch* touch, CCEvent* event)
{
	hudSprite->setTexture(texture_default);
	controlDirection = kNormal;
}

void ControlLayer::onExit()
{
	CCTouchDispatcher::sharedDispatcher()->removeDelegate(this);
	CCLayer::onExit();
}

void ControlLayer::reset()
{
	hudSprite->setTexture(texture_default);
	controlDirection = kNormal;
}