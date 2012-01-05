#include "GameLayer.h"

GameLayer::GameLayer(void)
{
	sGlobal->gameLayer = this;
}

GameLayer::~GameLayer(void)
{
	this->unscheduleAllSelectors();
}

bool GameLayer::init()
{
    bool bRet = false;
    do 
    {
		//解析tmx地图
		char temp[20];
		sprintf(temp, "%d.tmx", sGlobal->currentLevel);
		map = GameMap::gameMapWithTMXFile(temp);
		addChild(map, kZMap, kZMap);

		//调用Hero类的静态方法创建实例
		hero = Hero::heroWithinLayer();
		//设置Hero的起始位置
		hero->setPosition(map->positionForTileCoord(sGlobal->heroSpawnTileCoord));
		//将Hero加入GameLayer
		addChild(hero, kZHero, kZHero);

		schedule(schedule_selector(GameLayer::update));

        bRet = true;
    } while (0);

    return bRet;
}

void GameLayer::update(ccTime dt)
{
	//如果勇士不在行走状态，不需要更新场景位置
	if (hero->isHeroMoving)
	{
		setSceneScrollPosition(hero->getPosition());
	}
}

//传入勇士当前位置信息，将场景移动到相应位置
void GameLayer::setSceneScrollPosition(CCPoint position)
{
	//获取屏幕尺寸
	CCSize screenSize = CCDirector::sharedDirector()->getWinSize();
	//计算Tilemap的宽高，单位是像素
	CCSize mapSizeInPixel = CCSizeMake(map->getMapSize().width * map->getTileSize().width, 
		map->getMapSize().height * map->getTileSize().height);
	//取勇士当前x坐标和屏幕中点x的最大值，如果勇士的x值较大，则会滚动
	float x = MAX(position.x, screenSize.width / 2.0f);
	float y = MAX(position.y, screenSize.height / 2.0f);
	//地图总宽度大于屏幕宽度的时候才有可能滚动
	if (mapSizeInPixel.width  > screenSize.width)
	{
		//场景滚动的最大距离不能超过地图总宽减去屏幕宽的1/2
		x = MIN(x, mapSizeInPixel.width - screenSize.width / 2.0f);
	}
	if (mapSizeInPixel.height > screenSize.height)
	{
		y = MIN(y, mapSizeInPixel.height - screenSize.height / 2.0f);
	}
	//勇士的实际位置
	CCPoint heroPosition = ccp(x, y);
	//屏幕中点位置
	CCPoint screenCenter = ccp(screenSize.width/2.0f, screenSize.height/2.0f);
	//计算勇士实际位置和重点位置的距离
	CCPoint scrollPosition = ccpSub(screenCenter, heroPosition);
	//将场景移动到相应位置
	this->setPosition(scrollPosition);
	CCLog("%f,%f", scrollPosition.x, scrollPosition.y);
}

//显示提示信息
void GameLayer::showTip(const char *tip, CCPoint startPosition)
{
	//新建一个文本标签
	CCLabelTTF *tipLabel = CCLabelTTF::labelWithString(tip, "Arial", 20);
	tipLabel->setPosition(ccpAdd(startPosition, ccp(16, 16)));
	this->addChild(tipLabel, kZTip,kZTip);
	//定义动画效果
	CCAction* action = CCSequence::actions(
		CCMoveBy::actionWithDuration(0.5f, ccp(0, 32)),
		CCDelayTime::actionWithDuration(0.5f),
		CCFadeOut::actionWithDuration(0.2f),
		CCCallFuncN::actionWithTarget(this, callfuncN_selector(GameLayer::onShowTipDone)),
		NULL);
	tipLabel->runAction(action);
}

//提示消息显示完后的回调
void GameLayer::onShowTipDone(CCNode* pSender)
{
	//删掉文本标签
	this->getChildByTag(kZTip)->removeFromParentAndCleanup(true);
}