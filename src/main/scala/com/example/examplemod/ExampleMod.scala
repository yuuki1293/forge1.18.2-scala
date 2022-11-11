package com.example.examplemod

import com.mojang.logging.LogUtils
import net.minecraft.world.level.block.{Block, Blocks}
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.event.server.ServerStartingEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.InterModComms
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.{FMLCommonSetupEvent, InterModEnqueueEvent, InterModProcessEvent}
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext


// The value here should match an entry in the META-INF/mods.toml file
@Mod("examplemod") object ExampleMod { // Directly reference a slf4j logger
  private val LOGGER = LogUtils.getLogger

  // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
  // Event bus for receiving Registry Events)
  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD) object RegistryEvents {
    @SubscribeEvent def onBlocksRegistry(blockRegistryEvent: RegistryEvent.Register[Block]): Unit = { // Register a new block here
      LOGGER.info("HELLO from Register Block")
    }
  }
}

@Mod("examplemod") class ExampleMod() { // Register the setup method for modloading
  FMLJavaModLoadingContext.get.getModEventBus.addListener(this.setup)
  // Register the enqueueIMC method for modloading
  FMLJavaModLoadingContext.get.getModEventBus.addListener(this.enqueueIMC)
  // Register the processIMC method for modloading
  FMLJavaModLoadingContext.get.getModEventBus.addListener(this.processIMC)
  // Register ourselves for server and other game events we are interested in
  MinecraftForge.EVENT_BUS.register(this)

  private def setup(event: FMLCommonSetupEvent): Unit = { // some preinit code
    ExampleMod.LOGGER.info("HELLO FROM PREINIT")
    ExampleMod.LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName)
  }

  private def enqueueIMC(event: InterModEnqueueEvent): Unit = { // Some example code to dispatch IMC to another mod
    InterModComms.sendTo("examplemod", "helloworld", () => {
      def foo() = {
        ExampleMod.LOGGER.info("Hello world from the MDK")
        "Hello world"
      }

      foo()
    })
  }

  private def processIMC(event: InterModProcessEvent): Unit = { // Some example code to receive and process InterModComms from other mods
    ExampleMod.LOGGER.info("Got IMC {}", event.getIMCStream.map((m: InterModComms.IMCMessage) => m.messageSupplier.get).toList)
  }

  // You can use SubscribeEvent and let the Event Bus discover methods to call
  @SubscribeEvent def onServerStarting(event: ServerStartingEvent): Unit = { // Do something when the server starts
    ExampleMod.LOGGER.info("HELLO from server starting")
  }
}
