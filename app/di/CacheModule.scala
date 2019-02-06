package di

import cache.CacheService
import cache.impl.LocalCacheService
import com.google.inject.AbstractModule

class CacheModule extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[CacheService]).to(classOf[LocalCacheService])
  }

}
