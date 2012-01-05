#include "MTGame.h"

ControlLayer::ControlLayer(void)
{
	sGlobal->controlLayer = this;
}

ControlLayer::~ControlLayer(void)
{

}

bool ControlLayer::init()
{
	if ( !CCLayer::init() )
	{
		return false;
	}

	//�����رհ�ť
	CCMenuItemImage *pCloseItem = CCMenuItemImage::itemFromNormalImage(
		"CloseNormal.png",
		"CloseSelected.png",
		this,
		menu_selector(ControlLayer::menuCloseCallback));
	pCloseItem->setPosition(ccp(CCDirector::sharedDirector()->getWinSize().width - 20, 20));
	CCMenu* pMenu = CCMenu::menuWithItems(pCloseItem, NULL);
	pMenu->setPosition(CCPointZero);
	this->addChild(pMenu, 1);

	//��������ť
	CCMenuItem *down = CCMenuItemFont::itemFromString("down", this, menu_selector(ControlLayer::menuCallBackMove));
	CCMenuItem *left = CCMenuItemFont::itemFromString("left", this, menu_selector(ControlLayer::menuCallBackMove) );
	CCMenuItem *right = CCMenuItemFont::itemFromString("right", this, menu_selector(ControlLayer::menuCallBackMove) );
	CCMenuItem *up = CCMenuItemFont::itemFromString("up", this, menu_selector(ControlLayer::menuCallBackMove) );
	CCMenu* menu = CCMenu::menuWithItems(down, left, right, up, NULL);
	//Ϊ�˷�����ң���ÿ��menuItem����tag
	down->setTag(kDown);
	left->setTag(kLeft);
	right->setTag(kRight);
	up->setTag(kUp);
	//�˵�����50ˮƽ����
	menu->alignItemsHorizontallyWithPadding(50);
	addChild(menu);
	return true;
}

void ControlLayer::menuCloseCallback(CCObject* pSender)
{
    CCDirector::sharedDirector()->end();
}

void ControlLayer::menuCallBackMove(CCObject* pSender)
{
    CCNode *node = (CCNode *) pSender;
	//��ť��tag������Ҫ���ߵķ���
	int targetDirection = node->getTag();
	sGlobal->hero->move((HeroDirection) targetDirection);
}