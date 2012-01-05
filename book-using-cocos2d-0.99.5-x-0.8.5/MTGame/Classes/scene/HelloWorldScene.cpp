 #include "HelloWorldScene.h"

using namespace cocos2d;

HelloWorld::HelloWorld()
{

}

HelloWorld::~HelloWorld(void)
{
	for (int i = 0; i < 4; i++)
	{
		//释放数组中元素
		CC_SAFE_RELEASE(walkAnimation[i])
	}
	//释放数组本身
	CC_SAFE_DELETE_ARRAY(walkAnimation);

	this->unscheduleAllSelectors();
}

CCScene* HelloWorld::scene()
{
    CCScene * scene = NULL;
    do 
    {
        scene = CCScene::node();
        CC_BREAK_IF(! scene);

        HelloWorld *layer = HelloWorld::node();
        CC_BREAK_IF(! layer);

        scene->addChild(layer);
    } while (0);

    return scene;
}

bool HelloWorld::init()
{
    bool bRet = false;
    do 
    {
        CCMenuItemImage *pCloseItem = CCMenuItemImage::itemFromNormalImage(
            "CloseNormal.png",
            "CloseSelected.png",
            this,
            menu_selector(HelloWorld::menuCloseCallback));
        CC_BREAK_IF(! pCloseItem);
        pCloseItem->setPosition(ccp(CCDirector::sharedDirector()->getWinSize().width - 20, 20));
        CCMenu* pMenu = CCMenu::menuWithItems(pCloseItem, NULL);
        pMenu->setPosition(CCPointZero);
        CC_BREAK_IF(! pMenu);
        this->addChild(pMenu, 1);
		
		//解析tmx地图
		map = CCTMXTiledMap::tiledMapWithTMXFile("0.tmx");
		addChild(map);

		CCArray * pChildrenArray = map->getChildren();
		CCSpriteBatchNode* child = NULL;
		CCObject* pObject = NULL;
		//遍历Tilemap的所有图层
		CCARRAY_FOREACH(pChildrenArray, pObject)
		{
			child = (CCSpriteBatchNode*)pObject;
			if(!child)
				break;
			//给图层的纹理开启抗锯齿
			child->getTexture()->setAntiAliasTexParameters();
		}

		walkAnimation = new CCAnimation*[4];
		walkAnimation[kDown] = createAnimationByDirection(kDown);
		walkAnimation[kRight] = createAnimationByDirection(kRight);
		walkAnimation[kLeft] = createAnimationByDirection(kLeft);
		walkAnimation[kUp] = createAnimationByDirection(kUp);

		//用frame0作为勇士的静态图
		heroSprite = CCSprite::spriteWithSpriteFrame(walkAnimation[kDown]->getFrames()->getObjectAtIndex(0));
		//heroSprite->setPosition(ccp(48, 48));
		heroSprite->setAnchorPoint(CCPointZero);
		heroSprite->setPosition(positionForTileCoord(ccp(1, 11)));
		addChild(heroSprite);
		isHeroWalking = false;

		CCMenuItem *down = CCMenuItemFont::itemFromString("down", this, menu_selector(HelloWorld::menuCallBackMove));
		CCMenuItem *left = CCMenuItemFont::itemFromString("left", this, menu_selector(HelloWorld::menuCallBackMove) );
		CCMenuItem *right = CCMenuItemFont::itemFromString("right", this, menu_selector(HelloWorld::menuCallBackMove) );
		CCMenuItem *up = CCMenuItemFont::itemFromString("up", this, menu_selector(HelloWorld::menuCallBackMove) );
		CCMenu* menu = CCMenu::menuWithItems(down, left, right, up, NULL);
		//为了方便查找，给每个menuItem设置tag
		down->setTag(kDown);
		left->setTag(kLeft);
		right->setTag(kRight);
		up->setTag(kUp);
		//菜单项按间距50水平排列
		menu->alignItemsHorizontallyWithPadding(50);
		addChild(menu);

		schedule(schedule_selector(HelloWorld::update));

        bRet = true;
    } while (0);

    return bRet;
}

void HelloWorld::menuCloseCallback(CCObject* pSender)
{
    CCDirector::sharedDirector()->end();
}

CCAnimation* HelloWorld::createAnimationByDirection(HeroDirection direction)
{
	CCTexture2D *heroTexture = CCTextureCache::sharedTextureCache()->addImage("hero.png");
	CCSpriteFrame *frame0, *frame1, *frame2, *frame3;
	//第二个参数表示显示区域的x, y, width, height，根据direction来确定显示的y坐标
	frame0 = CCSpriteFrame::frameWithTexture(heroTexture, cocos2d::CCRectMake(32*0, 32*direction, 32, 32));
	frame1 = CCSpriteFrame::frameWithTexture(heroTexture, cocos2d::CCRectMake(32*1, 32*direction, 32, 32));
	frame2 = CCSpriteFrame::frameWithTexture(heroTexture, cocos2d::CCRectMake(32*2, 32*direction, 32, 32));
	frame3 = CCSpriteFrame::frameWithTexture(heroTexture, cocos2d::CCRectMake(32*3, 32*direction, 32, 32));
	CCMutableArray<CCSpriteFrame*>* animFrames = new CCMutableArray<CCSpriteFrame*>(4);
	animFrames->addObject(frame0);
	animFrames->addObject(frame1);
	animFrames->addObject(frame2);
	animFrames->addObject(frame3);
	CCAnimation* animation = new CCAnimation();
	//0.2f表示每帧动画间的间隔
	animation->initWithFrames(animFrames, 0.07f);
	animFrames->release();

	return animation;
}

void HelloWorld::menuCallBackMove(CCObject* pSender)
{
	if (isHeroWalking)
		return;

    CCNode *node = (CCNode *) pSender;
	//按钮的tag就是需要行走的方向
	int targetDirection = node->getTag();
	//移动的距离
	CCPoint moveByPosition;
	//根据方向计算移动的距离
	switch (targetDirection)
	{
	case kDown:
		moveByPosition = ccp(0, -32);
		break;
	case kLeft:
		moveByPosition = ccp(-32, 0);
		break;
	case kRight:
		moveByPosition = ccp(32, 0);
		break;
	case kUp:
		moveByPosition = ccp(0, 32);
		break;
	}
	//计算目标坐标，用当前勇士坐标加上移动距离
	CCPoint targetPosition = ccpAdd(heroSprite->getPosition(), moveByPosition);
	//调用checkCollision检测碰撞类型，如果是墙壁，则只需要设置勇士的朝向
	if (checkCollision(targetPosition) == kWall)
	{
		setFaceDirection((HeroDirection)targetDirection);
		return;
	}

	//利用CCSpawn将行走动画和移动同时执行
	CCAction *action = CCSequence::actions(
			CCSpawn::actions(
				CCAnimate::actionWithAnimation(walkAnimation[targetDirection], false),
				CCMoveBy::actionWithDuration(0.28f, moveByPosition),
				NULL),
			//把方向信息传递给onWalkDone方法
			CCCallFuncND::actionWithTarget(this, callfuncND_selector(HelloWorld::onWalkDone), (void*)targetDirection),
			NULL);
	heroSprite->runAction(action);
	isHeroWalking = true;
}

void HelloWorld::setFaceDirection(HeroDirection direction)
{
	heroSprite->setTextureRect(CCRectMake(0,32*direction,32,32));
}

void HelloWorld::onWalkDone(CCNode* pTarget, void* data)
{
	//将void*先转换为int，再从int转换到枚举类型
	int direction = (int) data;
	setFaceDirection((HeroDirection)direction);
	isHeroWalking = false;
	//heroSprite->setPosition(targetPosition);
	setSceneScrollPosition(heroSprite->getPosition());
}

//从cocos2d-x坐标转换为Tilemap坐标
CCPoint HelloWorld::tileCoordForPosition(CCPoint position)
{
	int x = position.x / map->getTileSize().width;
	int y = (((map->getMapSize().height - 1) * map->getTileSize().height) - position.y) / map->getTileSize().height;
	return ccp(x, y);
}

//从Tilemap坐标转换为cocos2d-x坐标
CCPoint HelloWorld::positionForTileCoord(CCPoint tileCoord)
{
	CCPoint pos =  ccp((tileCoord.x * map->getTileSize().width),
		((map->getMapSize().height - tileCoord.y - 1) * map->getTileSize().height));
	return pos;
}

//传入勇士当前位置信息，将场景移动到相应位置
void HelloWorld::setSceneScrollPosition(CCPoint position)
{
	//获取屏幕尺寸
	CCSize screenSize = CCDirector::sharedDirector()->getWinSize();
	//取勇士当前x坐标和屏幕中点x的最大值，如果勇士的x值较大，则会滚动
	float x = MAX(position.x, screenSize.width / 2.0f);
	float y = MAX(position.y, screenSize.height / 2.0f);
	//地图总宽度大于屏幕宽度的时候才有可能滚动
	if (map->getMapSize().width > screenSize.width)
	{
		//场景滚动的最大距离不能超过地图总宽减去屏幕宽的1/2
		x = MIN(x, (map->getMapSize().width * map->getTileSize().width) 
			- screenSize.width / 2.0f);
	}
	if (map->getMapSize().height > screenSize.height)
	{
		y = MIN(y, (map->getMapSize().height * map->getTileSize().height) 
			- screenSize.height / 2.0f);
	}
	//勇士的实际位置
	CCPoint heroPosition = ccp(x, y);
	//屏幕中点位置
	CCPoint screenCenter = ccp(screenSize.width/2.0f, screenSize.height/2.0f);
	//计算勇士实际位置和重点位置的距离
	CCPoint scrollPosition = ccpSub(screenCenter, heroPosition);
	//将场景移动到相应位置
	setPosition(scrollPosition);
	CCLog("scene position: %f,%f", scrollPosition.x, scrollPosition.y);
}

void HelloWorld::update(ccTime dt)
{
	//如果勇士不在行走状态，不需要更新场景位置
	if (isHeroWalking)
	{
		setSceneScrollPosition(heroSprite->getPosition());
	}
}

//判断碰撞类型
CollisionType HelloWorld::checkCollision(CCPoint heroPosition) 
{
	//cocos2d-x坐标转换为Tilemap坐标
	CCPoint tileCoord = tileCoordForPosition(heroPosition);
	//如果勇士坐标超过地图边界，返回kWall类型，阻止其移动
	if (heroPosition.x < 0 || tileCoord.x > map->getMapSize().width - 1 || tileCoord.y < 0 || tileCoord.y > map->getMapSize().height - 1)
		return kWall;
	//获取当前坐标位置的图块ID
	int tileGid = map->layerNamed("wall")->tileGIDAt(tileCoord);
	//如果图块ID不为0，表示有墙
	if (tileGid) {
		return kWall;
	}
	//可以通行
	return kNone;
}